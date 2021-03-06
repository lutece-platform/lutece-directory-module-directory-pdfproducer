/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.directory.modules.pdfproducer.service;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * DirectoryPDFProducerResourceIdService
 *
 */
public class DirectoryPDFProducerResourceIdService extends ResourceIdService
{
    /** Permission for generate zip */
    public static final String PERMISSION_GENERATE_PDF = "PDFPRODUCER";
    public static final String PERMISSION_MANAGE_PDFPRODUCER = "MANAGE_PDFPRODUCER";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "directory.permission.label.resource_type_directory";
    private static final String PROPERTY_LABEL_GENERATE_PDF = "module.directory.pdfproducer.permission.label.generate_pdf";
    private static final String PROPERTY_LABEL_MANAGE_PDFPRODUCER = "module.directory.pdfproducer.permission.label.manage_pdfproducer";

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        return DirectoryHome.getDirectoryList( PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(  )
    {
        // Override the resource type DIRECTORY_DIRECTORY_TYPE
        ResourceType rt = ResourceTypeManager.getResourceType( Directory.RESOURCE_TYPE );

        if ( rt == null )
        {
            rt = new ResourceType(  );
            rt.setResourceIdServiceClass( DirectoryResourceIdService.class.getName(  ) );
            rt.setPluginName( DirectoryPlugin.PLUGIN_NAME );
            rt.setResourceTypeKey( Directory.RESOURCE_TYPE );
            rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );
        }

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_GENERATE_PDF );
        p.setPermissionTitleKey( PROPERTY_LABEL_GENERATE_PDF );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MANAGE_PDFPRODUCER );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_PDFPRODUCER );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }
}
