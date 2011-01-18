/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.component;

import org.richfaces.cdk.annotations.*;
import org.richfaces.renderkit.html.TogglePanelItemRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;

/**
 * @author akolonitsky
 * @version 1.0
 */
@JsfComponent(
        tag = @Tag(type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.TogglePanelItemRenderer"))
public abstract class AbstractTogglePanelItem extends AbstractDivPanel {

    public static final String COMPONENT_TYPE = "org.richfaces.TogglePanelItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.TogglePanelItem";
    private static final String NAME = "name";

    protected AbstractTogglePanelItem() {
        setRendererType("org.richfaces.TogglePanelItemRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public AbstractTogglePanel getParent() {
        return (AbstractTogglePanel) super.getParent();
    }

    public boolean isActive() {
        return getParent().isActiveItem(this);
    }

    @Override
    public Renderer getRenderer(FacesContext context) {
        return super.getRenderer(context);
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        if (getParent().isActiveItem(this)) {
            super.encodeAll(context);
        } else {
            switch (getSwitchType()) {
                case client:
                    hidePanelItem(this);

                    super.encodeAll(context);
                    break;

                case ajax:
                case server:
                    this.encodePlaceHolderWithJs(context);
                    break;

                default:
                    throw new IllegalStateException("Unknown switch type : " + getSwitchType());
            }
        }
    }

    public void encodePlaceHolderWithJs(FacesContext context) throws IOException {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        context.getApplication().publishEvent(context, PreRenderComponentEvent.class, this);

        if (this.getRendererType() != null) {
            TogglePanelItemRenderer renderer = (TogglePanelItemRenderer) this.getRenderer(context);
            if (renderer != null) {
                renderer.encodePlaceHolderWithJs(context, this);
            }
        }

        popComponentFromEL(context);
    }

    protected static void hidePanelItem(UIComponent item) {
        // TODO move to renderer
        Map<String,Object> attrs = item.getAttributes();
        Object style = attrs.get("style");
        attrs.put("style", "display:none; " + style);
    }

    // ------------------------------------------------ Component Attributes

    @Attribute(defaultValue = "getId()", generate = false)
    public String getName() {
        return (String) getStateHelper().eval(NAME, getId());
    }

    public void setName(String name) {
        getStateHelper().put(NAME, name);
    }

    @Attribute(defaultValue = "getParent().getSwitchType()")
    public abstract SwitchType getSwitchType();

    // ------------------------------------------------ Html Attributes

    @Attribute(events = @EventName("enter"))
    public abstract String getOnenter();

    @Attribute(events = @EventName("leave"))
    public abstract String getOnleave();

    @Attribute
    public abstract String getLang();

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getDir();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();


    public String toString() {
        return "TogglePanelItem {name: " + getName() + ", switchType: " + getSwitchType() + '}';
    }
}



