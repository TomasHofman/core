/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.console.client.shared.homepage;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.BootstrapContext;
import org.jboss.as.console.client.core.Header;
import org.jboss.as.console.client.core.MainLayoutPresenter;
import org.jboss.as.console.client.core.NameTokens;

/**
 * @author Harald Pehl
 */
public class HomepagePresenter extends Presenter<HomepagePresenter.MyView, HomepagePresenter.MyProxy> {

    @NoGatekeeper
    @ProxyCodeSplit
    @NameToken(NameTokens.HomepagePresenter)
    public interface MyProxy extends Proxy<HomepagePresenter>, Place {}


    public interface MyView extends View {

        void addSection(SectionData section);

        void addSidebarSection(SidebarSectionData sidebarSection);
    }


    public static final Object SECTIONS_SLOT = new Object();
    public static final Object SIDEBAR_SECTIONS_SLOT = new Object();
    private final List<SectionData> sections;
    private final List<SidebarSectionData> sidebarSections;
    private final Header header;

    @Inject
    public HomepagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
            final PlaceManager placeManager, final BootstrapContext bootstrapContext, final Header header) {

        super(eventBus, view, proxy, MainLayoutPresenter.TYPE_MainContent);
        this.sections = setupSections(placeManager, bootstrapContext.isStandalone());
        this.sidebarSections = setupSidebarSection(bootstrapContext.isStandalone());
        this.header = header;
    }

    private List<SectionData> setupSections(final PlaceManager placeManager, final boolean standalone) {
        List<SectionData> sections = new LinkedList<SectionData>();
        if (standalone) {

            // Configuration
            SimpleContentBox dsBox = new SimpleContentBox(Console.CONSTANTS.content_box_create_datasource_title(),
                    Console.MESSAGES.content_box_create_datasource_body(), "Datasources",
                    NameTokens.DataSourcePresenter);
            sections.add(new SectionData("Configuration", Console.CONSTANTS.common_label_configuration(),
                    Console.CONSTANTS.section_configuration_intro(), true, dsBox));

            // Runtime
            String token = placeManager.buildHistoryToken(
                    new PlaceRequest.Builder().nameToken(NameTokens.DeploymentBrowserPresenter).with("new", "true")
                            .build());
            SimpleContentBox deployBox = new SimpleContentBox(Console.CONSTANTS.content_box_new_deployment_title(),
                    Console.MESSAGES.content_box_new_deployment_body(),
                    Console.CONSTANTS.content_box_new_deployment_link(), token);
            SimpleContentBox patchBox = new SimpleContentBox(Console.CONSTANTS.content_box_apply_patch_title(),
                    Console.MESSAGES.content_box_apply_patch_body(), "Patch Management", NameTokens.PatchingPresenter);
            sections.add(
                    new SectionData("Runtime", "Runtime", Console.CONSTANTS.section_runtime_intro(), false, deployBox,
                            patchBox));
        } else {

            // Configuration
            sections.add(new SectionData("Configuration", Console.CONSTANTS.common_label_configuration(),
                    Console.CONSTANTS.section_configuration_intro(), true));

            // Domain
            sections.add(new SectionData("Domain", "Domain", Console.CONSTANTS.section_domain_intro(), false));

            // Runtime
            sections.add(new SectionData("Runtime", "Runtime", Console.CONSTANTS.section_runtime_intro(), false));

        }

        // Administration
        SimpleContentBox roleAssignmentBox = new SimpleContentBox(Console.CONSTANTS.content_box_role_assignment_title(),
                Console.MESSAGES.content_box_role_assignment_body(),
                Console.CONSTANTS.content_box_role_assignment_link(), NameTokens.RoleAssignmentPresenter);
        sections.add(
                new SectionData("Administration", "Administration", Console.CONSTANTS.section_administration_intro(),
                        false, roleAssignmentBox));

        return sections;
    }

    private List<SidebarSectionData> setupSidebarSection(final boolean standalone) {
        List<SidebarSectionData> sections = new LinkedList<SidebarSectionData>();
        return sections;
    }

    @Override
    protected void onBind() {
        super.onBind();
        for (SectionData section : sections) {
            getView().addSection(section);
        }
        for (SidebarSectionData sidebarSection : sidebarSections) {
            getView().addSidebarSection(sidebarSection);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        header.highlight(NameTokens.HomepagePresenter);
    }
}