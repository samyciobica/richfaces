/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.region;

import java.util.Arrays;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunAsClient
@RunWith(Arquillian.class)
public class ITExecuteRichAjax extends AbstractRegionTest {

    @FindBy(id = "buttonNull")
    private WebElement buttonNull;

    @FindBy(id = "buttonThis")
    private WebElement buttonThis;

    @FindBy(id = "buttonAll")
    private WebElement buttonAll;

    @FindBy(id = "buttonForm")
    private WebElement buttonForm;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RegionTestDeployment deployment = new RegionTestDeployment(ITExecuteRichAjax.class);
        deployment.archive().addAsWebResource(ITExecuteRichAjax.class.getResource("ExecuteRichAjax.xhtml"),
            "index.xhtml");
        return deployment.getFinalArchive();
    }

    @Test
    public void testDefaults() {
        openPage(null);
        verifyExecutedIds(buttonNull, "buttonNull");
    }

    @Test
    public void testExecuteThis() {
        openPage("@this");
        verifyExecutedIds(buttonThis, "buttonThis");
    }

    @Test
    public void testExecuteAll() {
        openPage("@all");
        verifyExecutedIds(buttonAll, "@all");
    }

    @Test
    public void testExecuteForm() {
        openPage("@form");
        verifyExecutedIds(buttonForm, FORM_ID, "buttonForm");
    }

    protected void verifyExecutedIds(WebElement button, String... expectedExecutedIds) {
        Graphene.guardAjax(button).click();
        Assert.assertEquals(Arrays.toString(expectedExecutedIds), output.getText());
    }

}
