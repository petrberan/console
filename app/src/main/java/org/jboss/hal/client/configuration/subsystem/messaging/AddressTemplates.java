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
package org.jboss.hal.client.configuration.subsystem.messaging;

import org.jboss.hal.meta.AddressTemplate;

import static org.jboss.hal.meta.SelectionAwareStatementContext.SELECTION_EXPRESSION;

/**
 * @author Harald Pehl
 */
interface AddressTemplates {

    String MESSAGING_SUBSYSTEM_ADDRESS = "/{selected.profile}/subsystem=messaging-activemq";
    String SERVER_ADDRESS = MESSAGING_SUBSYSTEM_ADDRESS + "/server=*";
    String SELECTED_SERVER_ADDRESS = MESSAGING_SUBSYSTEM_ADDRESS + "/server=" + SELECTION_EXPRESSION;

    AddressTemplate MESSAGING_SUBSYSTEM_TEMPLATE = AddressTemplate.of(MESSAGING_SUBSYSTEM_ADDRESS);
    AddressTemplate SERVER_TEMPLATE = AddressTemplate.of(SERVER_ADDRESS);
    AddressTemplate SELECTED_SERVER_TEMPLATE = AddressTemplate.of(SELECTED_SERVER_ADDRESS);
}
