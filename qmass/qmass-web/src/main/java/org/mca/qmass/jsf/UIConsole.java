/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.jsf;

import javax.faces.component.ActionSource2;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 16:48
 */
@FacesComponent(value = "UIConsole")
public class UIConsole extends UICommand {

    public String getCommand() {
        return (String) getStateHelper().eval("command");
    }

    public void setCommand(String command) {
        getStateHelper().put("command", command);
        getValueExpression("command").setValue(FacesContext.getCurrentInstance().getELContext(), command);
    }

    public String getOutput() {
        return (String) getStateHelper().eval("output");
    }

    public void setOutput(String output) {
        getStateHelper().put("output", output);
    }

    @Override
    public String getFamily() {
        return "qmass.jsf.Console";
    }
}
