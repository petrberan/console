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
package org.jboss.hal.client.configuration.subsystem;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.hal.core.ProfileSelectionEvent;
import org.jboss.hal.core.finder.Finder;
import org.jboss.hal.core.finder.FinderPath;
import org.jboss.hal.core.mvp.ApplicationPresenter;
import org.jboss.hal.core.mvp.PatternFlyView;
import org.jboss.hal.dmr.Property;
import org.jboss.hal.dmr.model.ResourceAddress;
import org.jboss.hal.meta.AddressTemplate;
import org.jboss.hal.meta.StatementContext;
import org.jboss.hal.meta.token.NameTokens;

import static org.jboss.hal.dmr.ModelDescriptionConstants.PROFILE;

/**
 * Presenter for subsystems w/o a specific implementation in HAL. Relies on the model browser to manage a (sub)tree of
 * the management model starting at the resource specified as place request parameter.
 *
 * @author Harald Pehl
 */
public class GenericSubsystemPresenter
        extends ApplicationPresenter<GenericSubsystemPresenter.MyView, GenericSubsystemPresenter.MyProxy> {

    // @formatter:off
    @ProxyStandard
    @NameToken(NameTokens.GENERIC_SUBSYSTEM)
    public interface MyProxy extends ProxyPlace<GenericSubsystemPresenter> {}

    public interface MyView extends PatternFlyView {
        void setRoot(ResourceAddress root);
    }
    // @formatter:on


    public final static String ADDRESS_PARAM = "address";

    private final StatementContext statementContext;
    private ResourceAddress address;

    @Inject
    public GenericSubsystemPresenter(final EventBus eventBus,
            final MyView view,
            final MyProxy proxy,
            final Finder finder,
            final StatementContext statementContext) {
        super(eventBus, view, proxy, finder);
        this.statementContext = statementContext;
        this.address = ResourceAddress.ROOT;
    }

    @Override
    public void prepareFromRequest(final PlaceRequest request) {
        String parameter = request.getParameter(ADDRESS_PARAM, null);
        if (parameter != null) {
            address = AddressTemplate.of(parameter).resolve(statementContext);
        } else {
            address = ResourceAddress.ROOT;
        }
        for (Property property : address.asPropertyList()) {
            if (PROFILE.equals(property.getName())) {
                getEventBus().fireEvent(new ProfileSelectionEvent(property.getValue().asString()));
            }
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        getView().setRoot(address);
    }

    @Override
    protected FinderPath finderPath() {
        return FinderPath.subsystemPath(profile(), address.lastValue());
    }

    private String profile() {
        if (address != null) {
            for (Property property : address.asPropertyList()) {
                if (PROFILE.equals(property.getName())) {
                    return property.getValue().asString();
                }
            }
        }
        return null;
    }
}