/*
 * Copyright 2011 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import com.vaadin.server.VaadinRequest;
import com.vaadin.tests.VaadinClasses;
import com.vaadin.tests.components.AbstractTestUI;
import com.vaadin.tests.util.Log;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

public class UISerialization extends AbstractTestUI {

    private Log log = new Log(5);

    @Override
    protected void setup(VaadinRequest request) {
        addComponent(log);
        for (Class<? extends Component> cls : VaadinClasses.getComponents()) {
            try {
                AbstractComponent c = (AbstractComponent) cls.newInstance();
                if (c instanceof LegacyWindow) {
                    continue;
                }
                if (!(c instanceof Button)) {
                    continue;
                }

                c.setId(cls.getName());
                c.setCaption(cls.getName());
                c.setDescription(cls.getName());
                c.setWidth("100px");
                c.setHeight("100px");
                addComponent(c);
                System.out.println("Added " + cls.getName());
            } catch (Exception e) {
                System.err.println("Could not instatiate " + cls.getName());
            }
        }

        addComponent(new Button("Serialize UI", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Date d = new Date();
                byte[] result = serialize(UISerialization.this);
                long elapsed = new Date().getTime() - d.getTime();
                log.log("Serialized UI in " + elapsed + "ms into "
                        + result.length + " bytes");

            }
        }));
        addComponent(new Button(
                "Instantiate and serialize server classes with no-arg constructors",
                new ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        for (Class<?> cls : VaadinClasses
                                .getAllServerSideClasses()) {
                            try {
                                serializeInstance(cls);
                            } catch (InstantiationException e) {
                                // No no-arg constructor probably, ignore
                            } catch (Throwable t) {
                                log.log("Failed to create and serialize instance of "
                                        + cls.getName());
                            }
                        }
                        log.log("Serialization done");

                    }
                }));
    }

    protected void serializeInstance(Class<?> cls)
            throws InstantiationException, IllegalAccessException {
        serialize((Serializable) cls.newInstance());
    }

    protected byte[] serialize(Serializable serializable) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(serializable);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    @Override
    protected String getTestDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Integer getTicketNumber() {
        // TODO Auto-generated method stub
        return null;
    }

}
