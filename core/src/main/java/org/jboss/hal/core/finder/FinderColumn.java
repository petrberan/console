/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.core.finder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.HandlerRegistration;
import elemental2.dom.DragEvent;
import elemental2.dom.Element;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.NodeList;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.EventCallbackFn;
import org.jboss.gwt.elemento.core.IsElement;
import org.jboss.gwt.elemento.core.Key;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;
import org.jboss.hal.ballroom.Attachable;
import org.jboss.hal.ballroom.JsHelper;
import org.jboss.hal.ballroom.Tooltip;
import org.jboss.hal.core.Strings;
import org.jboss.hal.meta.security.AuthorisationDecision;
import org.jboss.hal.meta.security.Constraint;
import org.jboss.hal.meta.security.Constraints;
import org.jboss.hal.resources.CSS;
import org.jboss.hal.resources.Constants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.UIConstants;
import org.jboss.hal.spi.Callback;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;
import static org.jboss.gwt.elemento.core.Elements.*;
import static org.jboss.gwt.elemento.core.Elements.header;
import static org.jboss.gwt.elemento.core.EventType.bind;
import static org.jboss.gwt.elemento.core.EventType.click;
import static org.jboss.gwt.elemento.core.EventType.keydown;
import static org.jboss.gwt.elemento.core.EventType.keyup;
import static org.jboss.gwt.elemento.core.InputType.text;
import static org.jboss.gwt.elemento.core.Key.ArrowUp;
import static org.jboss.gwt.elemento.core.Key.Escape;
import static org.jboss.hal.core.finder.Finder.DATA_BREADCRUMB;
import static org.jboss.hal.core.finder.Finder.DATA_FILTER;
import static org.jboss.hal.resources.CSS.*;
import static org.jboss.hal.resources.Names.NOT_AVAILABLE;
import static org.jboss.hal.resources.UIConstants.GROUP;
import static org.jboss.hal.resources.UIConstants.ROLE;
import static org.jboss.hal.resources.UIConstants.TABINDEX;

/**
 * Describes a column in a finder. A column has an unique id, a title, a number of optional column actions
 * and an {@link ItemRenderer} which defines how the items of this column are rendered. All items of a column must have
 * the same type parameter which is the type parameter of this column.
 * <p>
 * The idea is that columns are self-contained and don't need direct references to other columns. References are only
 * provided by id. The {@link ColumnRegistry} will then resolve the id against an existing column.
 * <p>
 * Please do not use constants from {@code ModelDescriptionConstants} for the column ids (it makes refactoring harder).
 * Instead add an id to {@link org.jboss.hal.resources.Ids}.
 * <p>
 * TODO This class is huge! Try to refactor and break into smaller pieces.
 *
 * @param <T> The column and items type.
 */
public class FinderColumn<T> implements IsElement, Attachable {

    private static final String DOT = ".";
    private static final Constants CONSTANTS = GWT.create(Constants.class);
    @NonNls private static final Logger logger = LoggerFactory.getLogger(FinderColumn.class);

    private final Finder finder;
    private final String id;
    private final String title;
    private final boolean showCount;
    private final boolean pinnable;
    private final HTMLElement root;
    private final HTMLElement columnActions;
    private final HTMLElement hiddenColumns;
    private final HTMLElement headerElement;
    private final HTMLInputElement filterElement;
    private final HTMLElement clearFilterElement;
    private final HTMLElement ulElement;
    private final HTMLElement noItems;
    private final List<T> initialItems;
    private final ItemSelectionHandler<T> selectionHandler;
    private final List<HandlerRegistration> handlers;
    private final Map<String, FinderRow<T>> rows;
    private final FinderColumnStorage storage;

    private boolean asElement;
    private boolean firstActionAsBreadcrumbHandler;
    private ItemsProvider<T> itemsProvider;
    private ItemRenderer<T> itemRenderer;
    private PreviewCallback<T> previewCallback;
    private BreadcrumbItemsProvider<T> breadcrumbItemsProvider;
    private BreadcrumbItemHandler<T> breadcrumbItemHandler;


    // ------------------------------------------------------ ui

    protected FinderColumn(final Builder<T> builder) {
        this.finder = builder.finder;
        this.id = builder.id;
        this.title = builder.title;
        this.showCount = builder.showCount;
        this.pinnable = builder.pinnable;
        this.initialItems = builder.items;
        this.itemsProvider = builder.itemsProvider;
        this.itemRenderer = builder.itemRenderer;
        this.selectionHandler = builder.selectionHandler;
        this.previewCallback = builder.previewCallback;
        this.breadcrumbItemsProvider = builder.breadcrumbItemsProvider;
        this.breadcrumbItemHandler = builder.breadcrumbItemHandler;
        this.firstActionAsBreadcrumbHandler = builder.firstActionAsBreadcrumbHandler;
        this.asElement = false;

        this.rows = new HashMap<>();
        this.storage = new FinderColumnStorage(id);
        this.handlers = new ArrayList<>();

        // header
        HTMLElement header;
        root = div().css(finderColumn, column(2))
                .id(id)
                .attr(TABINDEX, "-1")
                .data(DATA_BREADCRUMB, title)
                .add(header = header()
                        .add(hiddenColumns = span().css(CSS.hiddenColumns, fontAwesome("angle-double-left"))
                                .title(CONSTANTS.hiddenColumns())
                                .data(UIConstants.TOGGLE, UIConstants.TOOLTIP)
                                .data(UIConstants.PLACEMENT, "bottom")
                                .asElement())
                        .add(headerElement = h(1, builder.title).title(builder.title).asElement())
                        .asElement())
                .asElement();

        // column actions
        List<ColumnAction<T>> allowedColumnActions = allowedActions(builder.columnActions);
        HtmlContentBuilder<HTMLDivElement> actionsBuilder = div();
        if (allowedColumnActions.size() == 1) {
            actionsBuilder.add(newColumnButton(allowedColumnActions.get(0)));
        } else {
            actionsBuilder.css(btnGroup).attr(ROLE, GROUP);
            for (ColumnAction<T> action : allowedColumnActions) {
                actionsBuilder.add(newColumnButton(action));
            }
        }
        columnActions = actionsBuilder.asElement();
        header.appendChild(columnActions);

        // filter box
        if (builder.withFilter) {
            String iconId = Ids.build(id, filter, "icon");
            HtmlContentBuilder<HTMLElement> clearFilter = span().css(inputGroupAddon, fontAwesome("close")).id(iconId)
                    .title(CONSTANTS.clear())
                    .on(click, event -> clearFilter());
            root.appendChild(
                    div().css(inputGroup, filter)
                            .add(filterElement = input(text).css(formControl)
                                    .id(Ids.build(id, filter))
                                    .aria(UIConstants.ARIA_DESCRIBEDBY, iconId)
                                    .attr(UIConstants.PLACEHOLDER,
                                            Strings.abbreviateMiddle(builder.filterDescription, 45))
                                    .attr(UIConstants.TITLE, builder.filterDescription)
                                    .asElement())
                            .add(clearFilter)
                            .asElement());
            clearFilterElement = clearFilter.asElement();
            Elements.setVisible(clearFilterElement, false);
        } else {
            filterElement = null;
            clearFilterElement = null;
        }

        // rows
        root.appendChild(ulElement = ul().asElement());
        if (pinnable) {
            ulElement.classList.add(CSS.pinnable);
        }

        // no items marker
        noItems = li().css(empty)
                .add(span().css(itemText).textContent(CONSTANTS.noItems()))
                .asElement();
    }

    @SuppressWarnings("Duplicates")
    private HTMLElement newColumnButton(final ColumnAction<T> action) {
        HtmlContentBuilder builder;
        if (!action.actions.isEmpty()) {
            HTMLElement button;
            HTMLElement ul;
            builder = div().css(dropdown)
                    .add(button = button().css(btn, btnFinder, dropdownToggle)
                            .id(action.id)
                            .data(UIConstants.TOGGLE, UIConstants.DROPDOWN)
                            .aria(UIConstants.EXPANDED, UIConstants.FALSE)
                            .asElement())
                    .add(ul = ul().css(dropdownMenu)
                            .id(Ids.uniqueId())
                            .attr(UIConstants.ROLE, UIConstants.MENU)
                            .aria(UIConstants.LABELLED_BY, action.id)
                            .asElement());
            if (action.title != null) {
                button.textContent = action.title;
            } else if (action.element != null) {
                button.appendChild(action.element);
            } else {
                button.textContent = NOT_AVAILABLE;
            }
            button.appendChild(span().css(caret).asElement());

            for (ColumnAction<T> liAction : action.actions) {
                HTMLElement a;
                ul.appendChild(li()
                        .attr(UIConstants.ROLE, UIConstants.PRESENTATION)
                        .add(a = a().id(liAction.id)
                                .attr(UIConstants.ROLE, UIConstants.MENUITEM)
                                .attr(UIConstants.TABINDEX, "-1")
                                .on(click, event -> {
                                    if (liAction.handler != null) {
                                        liAction.handler.execute(this);
                                    }
                                })
                                .asElement())
                        .asElement());
                if (liAction.title != null) {
                    a.textContent = liAction.title;
                } else if (liAction.element != null) {
                    a.appendChild(liAction.element);
                } else {
                    a.textContent = NOT_AVAILABLE;
                }
            }

        } else {
            builder = button().css(btn, btnFinder)
                    .id(action.id)
                    .on(click, event -> {
                        if (action.handler != null) {
                            action.handler.execute(this);
                        }
                    });
            if (action.title != null) {
                builder.textContent(action.title);
            } else if (action.element != null) {
                builder.add(action.element);
            } else {
                builder.textContent(NOT_AVAILABLE);
            }
        }
        return builder.asElement();
    }

    private void updateHeader(int matched) {
        if (showCount) {
            String titleWithSize;
            if (matched == rows.size()) {
                titleWithSize = title + " (" + rows.size() + ")";
            } else {
                titleWithSize = title + " (" + matched + " / " + rows.size() + ")";
            }
            headerElement.textContent = titleWithSize;
            headerElement.title = titleWithSize;
        }
    }

    @Override
    public void attach() {
        handlers.add(bind(root, keydown, this::onNavigation));
        handlers.add(bind(hiddenColumns, click, event -> finder.revealHiddenColumns(FinderColumn.this)));
        if (filterElement != null) {
            handlers.add(bind(filterElement, keydown, this::onNavigation));
            handlers.add(bind(filterElement, keyup, this::onFilter));
        }
    }

    @Override
    public void detach() {
        for (HandlerRegistration handler : handlers) {
            handler.removeHandler();
        }
        handlers.clear();
    }


    // ------------------------------------------------------ event handler

    private void onFilter(KeyboardEvent event) {
        if (Escape == Key.fromEvent(event)) {
            filterElement.value = "";
            // hide the 'clear' icon when there are no chars
            Elements.setVisible(clearFilterElement, false);
        } else {
            // show the 'clear' icon when there are typed chars
            Elements.setVisible(clearFilterElement, true);
        }

        int matched = 0;
        String filter = filterElement.value;
        for (HTMLElement li : Elements.children(ulElement)) {
            if (li == noItems) {
                continue;
            }
            Object filterData = li.dataset.get(DATA_FILTER);
            boolean match = filter == null
                    || filter.trim().length() == 0
                    || filterData == null
                    || String.valueOf(filterData).toLowerCase().contains(filter.toLowerCase());
            Elements.setVisible(li, match);
            if (match) {
                matched++;
            }
        }
        updateHeader(matched);
        if (matched == 0) {
            Elements.lazyAppend(ulElement, noItems);
        } else {
            Elements.failSafeRemove(ulElement, noItems);
        }
        // when user deletes remaining chars, hide the 'clear' icon
        if (filter.trim().length() == 0) {
            Elements.setVisible(clearFilterElement, false);
        }
    }

    private void clearFilter() {
        filterElement.value = "";
        for (HTMLElement li : Elements.children(ulElement)) {
            if (li == noItems) {
                continue;
            }
            Elements.setVisible(li, true);
        }
        Elements.failSafeRemove(ulElement, noItems);
        Elements.setVisible(clearFilterElement, false);
    }

    private void onNavigation(KeyboardEvent event) {
        if (hasVisibleElements()) {
            Key key = Key.fromEvent(event);
            switch (key) {

                case ArrowUp:
                case ArrowDown: {
                    HTMLElement activeElement = activeElement();
                    if (!Elements.isVisible(activeElement)) {
                        activeElement = null;
                    }
                    HTMLElement select = key == ArrowUp
                            ? previousVisibleElement(activeElement)
                            : nextVisibleElement(activeElement);
                    if (select != null && select != noItems) {
                        event.preventDefault();
                        event.stopPropagation();

                        select.scrollIntoView(false);
                        row(select).click();
                    }
                    break;
                }

                case ArrowLeft: {
                    HTMLElement previousElement = (HTMLElement) root.previousElementSibling;
                    if (previousElement != null) {
                        FinderColumn previousColumn = finder.getColumn(previousElement.id);
                        if (previousColumn != null) {
                            event.preventDefault();
                            event.stopPropagation();

                            Elements.setVisible(previousElement, true);
                            finder.reduceTo(previousColumn);
                            finder.selectColumn(previousColumn.getId());
                            FinderRow selectedRow = previousColumn.selectedRow();
                            if (selectedRow != null) {
                                selectedRow.updatePreview();
                                selectedRow.asElement().scrollIntoView(false);
                            }
                            finder.updateContext();
                            finder.updateHistory();
                        }
                    }
                    break;
                }

                case ArrowRight: {
                    HTMLElement activeElement = activeElement();
                    String nextColumn = row(activeElement).getNextColumn();
                    if (Elements.isVisible(activeElement) && nextColumn != null) {
                        event.preventDefault();
                        event.stopPropagation();

                        finder.reduceTo(this);
                        finder.appendColumn(nextColumn,
                                new AsyncCallback<FinderColumn>() {
                                    @Override
                                    public void onFailure(final Throwable throwable) {
                                        logger.error("Unable to append next column '{}' on keyboard right: {}",
                                                nextColumn, throwable.getMessage());
                                    }

                                    @Override
                                    public void onSuccess(final FinderColumn column) {
                                        if (column.activeElement() == null && column.hasVisibleElements()) {
                                            HTMLElement firstElement = column.nextVisibleElement(null);
                                            column.markSelected(firstElement.id);
                                            column.row(firstElement).updatePreview();
                                        }
                                        finder.updateContext();
                                        finder.updateHistory();
                                        finder.selectColumn(nextColumn);
                                    }
                                });
                    }
                    break;
                }

                case Enter: {
                    HTMLElement activeItem = activeElement();
                    T item = row(activeItem).getItem();
                    ItemActionHandler<T> primaryAction = row(activeItem).getPrimaryAction();
                    if (Elements.isVisible(activeItem) && item != null && primaryAction != null) {
                        event.preventDefault();
                        event.stopPropagation();

                        row(activeItem).click();
                        primaryAction.execute(item);
                    }
                    break;
                }

                default:
                    break;
            }
        }
    }

    protected boolean isVisible() {
        return asElement && Elements.isVisible(root) && root.parentNode != null;
    }


    // ------------------------------------------------------ internal API

    void markHiddenColumns(boolean show) {
        Elements.setVisible(hiddenColumns, show);
    }

    private HTMLElement activeElement() {
        return (HTMLElement) ulElement.querySelector("li." + active); //NON-NLS
    }

    private boolean hasVisibleElements() {
        for (HTMLElement element : Elements.children(ulElement)) {
            if (Elements.isVisible(element) && element != noItems) {
                return true;
            }
        }
        return false;
    }

    private HTMLElement previousVisibleElement(HTMLElement start) {
        HTMLElement element = (HTMLElement) (start == null ? ulElement.lastElementChild : start.previousElementSibling);
        while (element != null && !Elements.isVisible(element)) {
            element = (HTMLElement) element.previousElementSibling;
        }
        return element;
    }

    private HTMLElement nextVisibleElement(HTMLElement start) {
        HTMLElement element = (HTMLElement) (start == null ? ulElement.firstElementChild : start.nextElementSibling);
        while (element != null && !Elements.isVisible(element)) {
            element = (HTMLElement) element.nextElementSibling;
        }
        return element;
    }

    FinderRow<T> row(String itemId) {
        return rows.get(itemId);
    }

    private FinderRow<T> row(Element element) {
        if (element instanceof HTMLElement) {
            return row(((HTMLElement) element).id);
        }
        return null;
    }

    FinderRow<T> selectedRow() {
        HTMLElement activeItem = (HTMLElement) ulElement.querySelector("li." + active); //NON-NLS
        if (activeItem != null && rows.containsKey(activeItem.id)) {
            return rows.get(activeItem.id);
        }
        return null;
    }

    boolean contains(String itemId) {
        return rows.containsKey(itemId);
    }

    void markSelected(String itemId) {
        for (Map.Entry<String, FinderRow<T>> entry : rows.entrySet()) {
            boolean select = itemId.equals(entry.getKey());
            entry.getValue().markSelected(select);
            if (select && selectionHandler != null) {
                selectionHandler.onSelect(entry.getValue().getItem());
            }
        }
    }

    void resetSelection() {
        HTMLElement element = activeElement();
        if (element != null) {
            element.classList.remove(active);
        }
    }

    boolean isPinnable() {
        return pinnable;
    }

    void unpin(final FinderRow<T> row) {
        row.asElement().classList.remove(pinned);
        row.asElement().classList.add(unpinned);

        // move row to unpinned section
        ulElement.removeChild(row.asElement());
        NodeList<Element> nodes = ulElement.querySelectorAll(DOT + unpinned);
        if (nodes.getLength() == 0) {
            // no unpinned rows append to bottom
            ulElement.appendChild(row.asElement());
        } else {
            Element before = findPosition(nodes, row);
            if (before != null) {
                ulElement.insertBefore(row.asElement(), before);
            } else {
                ulElement.appendChild(row.asElement());
            }
        }
        adjustPinSeparator();
        storage.unpinItem(row.getId());
    }

    void pin(final FinderRow<T> row) {
        row.asElement().classList.remove(unpinned);
        row.asElement().classList.add(pinned);

        // move row to pinned section
        ulElement.removeChild(row.asElement());
        NodeList<Element> nodes = ulElement.querySelectorAll(DOT + pinned);
        if (nodes.getLength() == 0) {
            // no pinned rows append to top
            ulElement.insertBefore(row.asElement(), ulElement.firstChild);
        } else {
            Element before = findPosition(nodes, row);
            if (before != null) {
                ulElement.insertBefore(row.asElement(), before);
            } else {
                Element firstUnpinned = ulElement.querySelector(DOT + unpinned);
                if (firstUnpinned != null) {
                    ulElement.insertBefore(row.asElement(), firstUnpinned);
                } else {
                    ulElement.appendChild(row.asElement());
                }
            }
        }
        adjustPinSeparator();
        row.asElement().scrollIntoView(false);
        storage.pinItem(row.getId());
    }

    private Element findPosition(NodeList<Element> nodes, FinderRow<T> row) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Element currentElement = nodes.item(i);
            FinderRow<T> currentRow = row(currentElement);
            if (currentRow.getDisplay().getTitle().compareTo(row.getDisplay().getTitle()) > 0) {
                return currentElement;
            }
        }
        return null;
    }

    private void adjustPinSeparator() {
        NodeList<Element> nodes = ulElement.querySelectorAll(DOT + pinned);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = nodes.item(i);
            if (i == nodes.getLength() - 1) {
                element.classList.add(last);
            } else {
                element.classList.remove(last);
            }
        }
    }

    void setItems(AsyncCallback<FinderColumn> callback) {
        if (!initialItems.isEmpty()) {
            setItems(initialItems, callback);

        } else if (itemsProvider != null) {
            itemsProvider.get(finder.getContext(), new AsyncCallback<List<T>>() {
                @Override
                public void onFailure(final Throwable throwable) {
                    if (callback != null) {
                        callback.onFailure(throwable);
                    }
                }

                @Override
                public void onSuccess(final List<T> items) {
                    setItems(items, callback);
                }
            });

        } else {
            setItems(Collections.emptyList(), callback);
        }
    }

    private void setItems(List<T> items, AsyncCallback<FinderColumn> callback) {
        rows.clear();
        Elements.removeChildrenFrom(ulElement);
        if (filterElement != null) {
            filterElement.value = "";
        }

        List<T> pinnedItems = new ArrayList<>();
        List<T> unpinnedItems = new ArrayList<>();
        Set<String> pinnedItemIds = storage.pinnedItems();
        if (pinnable && !pinnedItemIds.isEmpty()) {
            for (T item : items) {
                String id = itemRenderer.render(item).getId();
                if (pinnedItemIds.contains(id)) {
                    pinnedItems.add(item);
                } else {
                    unpinnedItems.add(item);
                }
            }
        } else {
            unpinnedItems.addAll(items);
        }
        for (Iterator<T> iterator = pinnedItems.iterator(); iterator.hasNext(); ) {
            T item = iterator.next();
            FinderRow<T> row = new FinderRow<>(finder, this, item, true,
                    itemRenderer.render(item), previewCallback);
            rows.put(row.getId(), row);
            ulElement.appendChild(row.asElement());
            if (!iterator.hasNext()) {
                row.asElement().classList.add(last);
            }
        }
        for (T item : unpinnedItems) {
            FinderRow<T> row = new FinderRow<>(finder, this, item, false,
                    itemRenderer.render(item), previewCallback);
            rows.put(row.getId(), row);
            ulElement.appendChild(row.asElement());
        }
        updateHeader(items.size());
        Tooltip.select("#" + id + " [data-" + UIConstants.TOGGLE + "=" + UIConstants.TOOLTIP + "]").init(); //NON-NLS

        if (items.isEmpty()) {
            ulElement.appendChild(noItems);
        }

        if (callback != null) {
            callback.onSuccess(this);
        }
    }

    /**
     * Sometimes you need to reference {@code this} in the column action handler. This is not possible if they're
     * part of the builder which is passed to {@code super()}. In this case you can use this method to add your column
     * actions <strong>after</strong> the call to {@code super()}.
     */
    protected void addColumnAction(ColumnAction<T> columnAction) {
        if (isAllowed(columnAction)) {
            columnActions.appendChild(newColumnButton(columnAction));
            if (columnActions.childElementCount > 1) {
                columnActions.classList.add(btnGroup);
                columnActions.setAttribute(ROLE, GROUP);
            }
        }
    }

    protected void addColumnActions(String id, String iconsCss, String title, List<ColumnAction<T>> actions) {
        if (isAllowed(actions)) {
            HTMLElement element = span().css(iconsCss)
                    .title(title)
                    .data(UIConstants.TOGGLE, UIConstants.TOOLTIP)
                    .data(UIConstants.PLACEMENT, "bottom")
                    .asElement();
            ColumnAction<T> dropdownAction = new ColumnAction.Builder<T>(id)
                    .element(element)
                    .actions(actions)
                    .build();
            columnActions.appendChild(newColumnButton(dropdownAction));
            if (columnActions.childElementCount > 1) {
                columnActions.classList.add(btnGroup);
                columnActions.setAttribute(ROLE, GROUP);
            }
        }
    }

    protected void resetColumnActions() {
        Elements.removeChildrenFrom(columnActions);
    }

    /**
     * Sometimes you need to reference {@code this} in the actions created by {@link ItemDisplay#actions()}. This is
     * not possible if they're part of the builder which is passed to {@code super()}. In this case the item renderer
     * can be specified <strong>after</strong> the call to {@code super()} using this setter.
     * <p>
     * However make sure to call the setter <strong>before</strong> the column is used {@link #asElement()} and gets
     * attached to the DOM!
     */
    protected void setItemRenderer(final ItemRenderer<T> itemRenderer) {
        assertNotAsElement("setItemRenderer()");
        this.itemRenderer = itemRenderer;
    }

    ItemRenderer<T> getItemRenderer() {
        return itemRenderer;
    }

    /**
     * Sometimes you need to reference {@code this} in the items provider. This is not possible if the items provider
     * is part of the builder which is passed to {@code super()}. In this case the items provider can be specified
     * <strong>after</strong> the call to {@code super()} using this setter.
     * <p>
     * However make sure to call the setter <strong>before</strong> the column is used {@link #asElement()} and gets
     * attached to the DOM!
     */
    protected void setItemsProvider(final ItemsProvider<T> itemsProvider) {
        assertNotAsElement("setItemsProvider()");
        this.itemsProvider = itemsProvider;
    }

    ItemsProvider<T> getItemsProvider() {
        return itemsProvider;
    }

    List<T> getInitialItems() {
        return initialItems;
    }

    /**
     * Sometimes you need to reference {@code this} in the preview callback. This is not possible if the preview
     * callback is part of the builder which is passed to {@code super()}. In this case the preview callback can be
     * specified <strong>after</strong> the call to {@code super()} using this setter.
     * <p>
     * However make sure to call the setter <strong>before</strong> the column is used {@link #asElement()} and gets
     * attached to the DOM!
     */
    protected void setPreviewCallback(final PreviewCallback<T> previewCallback) {
        this.previewCallback = previewCallback;
    }

    /**
     * Sometimes you need to reference {@code this} in the breadcrumb items provider. This is not possible if the
     * breadcrumb items provider is part of the builder which is passed to {@code super()}. In this case the breadcrumb
     * items provider can be specified <strong>after</strong> the call to {@code super()} using this setter.
     * <p>
     * However make sure to call the setter <strong>before</strong> the column is used {@link #asElement()} and gets
     * attached to the DOM!
     */
    protected void setBreadcrumbItemsProvider(final BreadcrumbItemsProvider<T> breadcrumbItemsProvider) {
        assertNotAsElement("setBreadcrumbItemsProvider()");
        this.breadcrumbItemsProvider = breadcrumbItemsProvider;
    }

    BreadcrumbItemsProvider<T> getBreadcrumbItemsProvider() {
        return breadcrumbItemsProvider;
    }

    BreadcrumbItemHandler<T> getBreadcrumbItemHandler() {
        return breadcrumbItemHandler;
    }

    boolean useFirstActionAsBreadcrumbHandler() {
        return firstActionAsBreadcrumbHandler;
    }

    protected void setOnDrop(EventCallbackFn<DragEvent> handler) {
        handlers.add(JsHelper.addDropHandler(ulElement, handler));
    }

    private void assertNotAsElement(String method) {
        if (asElement) {
            throw new IllegalStateException("Illegal call to FinderColumn." + method +
                    " after FinderColumn.asElement(). Make sure to setup the column before it's used as an element.");
        }
    }


    // ------------------------------------------------------ public API

    @Override
    public HTMLElement asElement() {
        asElement = true;
        return root;
    }

    public void refresh(RefreshMode refreshMode) {
        switch (refreshMode) {
            case CLEAR_SELECTION:
                if (finder.columns() == 1) {
                    refresh(finder::showInitialPreview);
                } else {
                    refresh(() -> finder.selectPreviousColumn(id));
                }
                break;
            case RESTORE_SELECTION:
                FinderRow<T> oldRow = selectedRow();
                refresh(() -> {
                    if (oldRow != null) {
                        FinderRow<T> updatedRow = rows.get(oldRow.getId());
                        if (updatedRow != null) {
                            updatedRow.click();
                            updatedRow.asElement().scrollIntoView(false);
                        } else {
                            finder.selectPreviousColumn(id);
                        }
                    } else {
                        finder.selectPreviousColumn(id);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * Refreshes and selects and the specified item.
     */
    public void refresh(String selectItemId) {
        refresh(() -> {
            FinderRow<T> row = rows.get(selectItemId);
            if (row != null) {
                row.click();
            } else {
                finder.selectPreviousColumn(id);
            }
        });
    }

    public void refresh(Callback callback) {
        setItems(new AsyncCallback<FinderColumn>() {
            @Override
            public void onFailure(final Throwable throwable) {
                logger.error("Unable to refresh column {}: {}", id, throwable.getMessage());
            }

            @Override
            public void onSuccess(final FinderColumn column) {
                finder.updateContext();
                if (callback != null) {
                    callback.execute();
                }
            }
        });
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    protected Finder getFinder() {
        return finder;
    }


    // ------------------------------------------------------ rbac / security

    private List<ColumnAction<T>> allowedActions(final List<ColumnAction<T>> actions) {
        return actions.stream()
                .filter(action -> {
                    if (!action.actions.isEmpty()) {
                        Set<Constraint> collect = new HashSet<>();
                        Iterables.addAll(collect, action.constraints);
                        action.actions.forEach(a -> Iterables.addAll(collect, a.constraints));
                        return AuthorisationDecision.from(finder.environment(),
                                finder.securityContextRegistry()).isAllowed(Constraints.and(collect));
                    }
                    return AuthorisationDecision.from(finder.environment(),
                            finder.securityContextRegistry()).isAllowed(action.constraints);
                })
                .collect(toList());
    }

    private boolean isAllowed(List<ColumnAction<T>> actions) {
        // the action is allowed if all constraints are allowed
        Set<Constraint> collect = new HashSet<>();
        actions.forEach(a -> {
            Iterables.addAll(collect, a.constraints);
            a.actions.forEach(innerA -> Iterables.addAll(collect, innerA.constraints));
        });

        return AuthorisationDecision.from(finder.environment(), finder.securityContextRegistry())
                .isAllowed(Constraints.and(collect));
    }

    private boolean isAllowed(ColumnAction<T> action) {
        // the action is allowed if all constraints are allowed
        Set<Constraint> collect = new HashSet<>();
        Iterables.addAll(collect, action.constraints);
        action.actions.forEach(a -> Iterables.addAll(collect, a.constraints));

        return AuthorisationDecision.from(finder.environment(), finder.securityContextRegistry())
                .isAllowed(Constraints.and(collect));
    }


    public enum RefreshMode {
        CLEAR_SELECTION, RESTORE_SELECTION
    }


    public static class Builder<T> {

        private final Finder finder;
        private final String id;
        private final String title;
        private final List<ColumnAction<T>> columnActions;
        private ItemRenderer<T> itemRenderer;
        private boolean showCount;
        private boolean withFilter;
        private boolean pinnable;
        private PreviewCallback<T> previewCallback;
        private BreadcrumbItemHandler<T> breadcrumbItemHandler;
        private boolean firstActionAsBreadcrumbHandler;
        private List<T> items;
        private ItemsProvider<T> itemsProvider;
        private BreadcrumbItemsProvider<T> breadcrumbItemsProvider;
        private ItemSelectionHandler<T> selectionHandler;
        private String filterDescription;

        public Builder(final Finder finder, final String id, final String title) {
            this.finder = finder;
            this.id = id;
            this.title = title;
            this.itemRenderer = item -> () -> String.valueOf(item);
            this.columnActions = new ArrayList<>();
            this.showCount = false;
            this.withFilter = false;
            this.pinnable = false;
            this.items = new ArrayList<>();
            this.filterDescription = CONSTANTS.filter();
        }

        /**
         * Adds a single column action button in the header of the column
         */
        public Builder<T> columnAction(ColumnAction<T> action) {
            columnActions.add(action);
            return this;
        }

        public Builder<T> showCount() {
            this.showCount = true;
            return this;
        }

        public Builder<T> withFilter() {
            return withFilter(true);
        }

        public Builder<T> withFilter(boolean yesNo) {
            this.withFilter = yesNo;
            return this;
        }

        public Builder<T> filterDescription(String filterTooltip) {
            this.filterDescription = filterTooltip;
            return this;
        }

        public Builder<T> pinnable() {
            this.pinnable = true;
            return this;
        }

        public Builder<T> initialItems(List<T> items) {
            if (items != null && !items.isEmpty()) {
                this.items.addAll(items);
            }
            return this;
        }

        public Builder<T> itemsProvider(ItemsProvider<T> itemsProvider) {
            this.itemsProvider = itemsProvider;
            return this;
        }

        public Builder<T> breadcrumbItemsProvider(BreadcrumbItemsProvider<T> breadcrumbItemsProvider) {
            this.breadcrumbItemsProvider = breadcrumbItemsProvider;
            return this;
        }

        public Builder<T> itemRenderer(ItemRenderer<T> itemRenderer) {
            this.itemRenderer = itemRenderer;
            return this;
        }

        public Builder<T> onItemSelect(ItemSelectionHandler<T> selectionHandler) {
            this.selectionHandler = selectionHandler;
            return this;
        }

        public Builder<T> onPreview(PreviewCallback<T> previewCallback) {
            this.previewCallback = previewCallback;
            return this;
        }

        /**
         * Sets the handler which is executed when an item in the breadcrumb dropdown is selected. Has precedence over
         * {@link #useFirstActionAsBreadcrumbHandler()}.
         */
        public Builder<T> onBreadcrumbItem(BreadcrumbItemHandler<T> handler) {
            this.breadcrumbItemHandler = handler;
            return this;
        }

        /**
         * Uses the item's first action as breadcrumb item handler. If a custom handler is set using {@link
         * #onBreadcrumbItem(BreadcrumbItemHandler)} this handler will be used instead of the first item action.
         */
        public Builder<T> useFirstActionAsBreadcrumbHandler() {
            this.firstActionAsBreadcrumbHandler = true;
            return this;
        }

        public FinderColumn<T> build() {
            return new FinderColumn<>(this);
        }
    }
}
