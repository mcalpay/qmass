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

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 17:07
 */
@FacesRenderer(rendererType = "UIConsole", componentFamily = "qmass.jsf.Console")
@ResourceDependencies({
        @ResourceDependency(name = "qconsole.css", library = "org.mca.qmass", target = "head"),
        @ResourceDependency(name = "jsf.js", library = "javax.faces", target = "body")})
public class ConsoleRenderer extends Renderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        UIConsole console = (UIConsole) component;
        String val = context.getExternalContext().getRequestParameterMap().get(getCommandId(console));
        console.setCommand(val);
        console.queueEvent(new ActionEvent(component));
    }

    private String getCommandId(UIConsole console) {
        return console.getClientId() + "_in";
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        UIConsole comp = (UIConsole) component;
        String inId = getCommandId(comp);
        String lines = comp.getOutput();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("id", comp.getClientId(), null);
        writer.writeAttribute("name", comp.getClientId(), null);
        writer.writeAttribute("class", "qconsole", null);
        writer.writeAttribute("onmouseover", "document.getElementById('" + inId + "').focus();", null);
        String[] lineRay = lines.split("\n");
        for (int i = 0; i < lineRay.length; i++) {
            writer.startElement("div", null);
            writer.writeAttribute("class", "qconsolerow", null);
            writer.write(lineRay[i].replaceAll(" ", "&nbsp;"));
            if (i + 1 == lineRay.length) {
                writer.startElement("input", null);
                writer.writeAttribute("id", inId, null);
                writer.writeAttribute("name", inId, null);
                writer.writeAttribute("onkeypress",
                        "if(event.keyCode == 13){" +
                                "jsf.ajax.request(this,event,{execute:'" + comp.getClientId() + "'," +
                                "render:'" + comp.getClientId() + "'," +
                                "onevent:function(e) {if(e.status=='success')" +
                                "document.getElementById('" + inId + "').focus();}});" +
                                "return false;" +
                                "}",
                        null);
                writer.writeAttribute("class", "qconsole", null);
                writer.writeAttribute("autocomplete", "off", null);
                writer.writeAttribute("type", "text", null);
                writer.endElement("input");
            }
            writer.endElement("div");
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }
}
