/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
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
package org.jboss.as.console.client.shared.subsys.osgi.model;

import org.jboss.as.console.client.widgets.forms.Binding;

/**
 * This interface is implemented by an AutoBean to interface with the module sub-resource in the
 * detyped server API of the OSGi subsystem.
 * @author David Bosschaert
 */
public interface OSGiPreloadedModule {
    String getIdentifier();
    void setIdentifier(String id);

    // Start Level is really an integer, but it can also be ommitted and
    // the autobean handling of no value -> null doesn't work for Integers
    @Binding(detypedName = "start")
    String getStartLevel();
    void setStartLevel(String sl);
}
