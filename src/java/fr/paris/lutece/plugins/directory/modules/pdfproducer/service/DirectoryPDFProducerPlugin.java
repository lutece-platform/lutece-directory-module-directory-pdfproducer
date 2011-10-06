/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import fr.paris.lutece.plugins.directory.business.DirectoryAction;
import fr.paris.lutece.plugins.directory.business.DirectoryActionHome;
import fr.paris.lutece.plugins.directory.business.DirectoryRemovalListenerService;
import fr.paris.lutece.plugins.directory.business.EntryRemovalListenerService;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.business.listener.DirectoryPDFProducerRemovalListener;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.business.listener.EntryPDFProducerRemovalListener;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.portal.service.plugin.PluginService;


/**
 * DirectoryPDFProducerPlugin
 *
 */
public class DirectoryPDFProducerPlugin extends PluginDefaultImplementation
{
    public static final String PLUGIN_NAME = "directory-pdfproducer";
    public static final String DIRECTORY_ACTION_NAME_KEY = "module.directory.pdfproducer.actions.extractpdf.name";
    public static final String DIRECTORY_ACTION_DESCRIPTION = "module.directory.pdfproducer.actions.extractpdf.description";
    public static final String DIRECTORY_ACTION_URL = "jsp/admin/plugins/directory/modules/pdfproducer/action/DoDownloadPDF.jsp";
    public static final String DIRECTORY_ACTION_URL_ICON_PDF = "images/admin/skin/plugins/directory/modules/pdfproducer/pdf.png";
    public static final String DIRECTORY_ACTION_PERMISSION = "PDFPRODUCER";
    public static final int DIRECTORY_ACTION_STATE_0 = 0;
    public static final int DIRECTORY_ACTION_STATE_1 = 1;

    /**
     * {@inheritDoc}
     */
    public void init(  )
    {
        //ConfigProducerHome.addActionsDirectoryRecord( PluginService.getPlugin( PLUGIN_NAME ) );
        checkAndAddNewDirectoryRecordAction(  );

        DirectoryPDFProducerRemovalListener listenerDirectory = new DirectoryPDFProducerRemovalListener(  );
        EntryPDFProducerRemovalListener listenerEntry = new EntryPDFProducerRemovalListener(  );

        DirectoryRemovalListenerService.getService(  ).registerListener( listenerDirectory );
        EntryRemovalListenerService.getService(  ).registerListener( listenerEntry );
    }

    /**
     * {@inheritDoc}
     */
    public void install(  )
    {
        super.install(  );
        checkAndAddNewDirectoryRecordAction(  );
    }

    /**
     * {@inheritDoc}
     */
    public void uninstall(  )
    {
        super.uninstall(  );

        Plugin directoryPlugin = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        DirectoryAction directoryAction = initAction( DIRECTORY_ACTION_STATE_0 );

        if ( DirectoryActionHome.checkActionsDirectoryRecord( directoryAction, directoryPlugin ) )
        {
            DirectoryActionHome.deleteActionsDirectoryRecord( directoryAction, directoryPlugin );
        }

        directoryAction = initAction( DIRECTORY_ACTION_STATE_1 );

        if ( DirectoryActionHome.checkActionsDirectoryRecord( directoryAction, directoryPlugin ) )
        {
            DirectoryActionHome.deleteActionsDirectoryRecord( directoryAction, directoryPlugin );
        }
    }

    /**
     * This method add new directory record action
     */
    private static void checkAndAddNewDirectoryRecordAction(  )
    {
        Plugin directoryPlugin = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        DirectoryAction directoryAction = initAction( DIRECTORY_ACTION_STATE_0 );

        if ( !DirectoryActionHome.checkActionsDirectoryRecord( directoryAction, directoryPlugin ) )
        {
            DirectoryActionHome.addNewActionInDirectoryRecordAction( directoryAction, directoryPlugin );
        }

        directoryAction = initAction( DIRECTORY_ACTION_STATE_1 );

        if ( !DirectoryActionHome.checkActionsDirectoryRecord( directoryAction, directoryPlugin ) )
        {
            DirectoryActionHome.addNewActionInDirectoryRecordAction( directoryAction, directoryPlugin );
        }
    }

    /**
     * this method create a DirectoryAction
     * @param nFormState 0 or 1
     * @return DirectoryAction
     */
    private static DirectoryAction initAction( int nFormState )
    {
        DirectoryAction directoryAction = new DirectoryAction(  );
        directoryAction.setNameKey( DIRECTORY_ACTION_NAME_KEY );
        directoryAction.setDescriptionKey( DIRECTORY_ACTION_DESCRIPTION );
        directoryAction.setUrl( DIRECTORY_ACTION_URL );
        directoryAction.setIconUrl( DIRECTORY_ACTION_URL_ICON_PDF );
        directoryAction.setPermission( DIRECTORY_ACTION_PERMISSION );
        directoryAction.setFormState( nFormState );

        return directoryAction;
    }
}
