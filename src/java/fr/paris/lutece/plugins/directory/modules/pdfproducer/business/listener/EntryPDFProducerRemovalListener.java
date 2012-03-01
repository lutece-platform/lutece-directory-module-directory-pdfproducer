/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.pdfproducer.business.listener;

import fr.paris.lutece.plugins.directory.modules.pdfproducer.business.producerconfig.ConfigProducerHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.RemovalListener;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;


/**
 * EntryPDFProducerRemovalListener *
 */
public class EntryPDFProducerRemovalListener implements RemovalListener
{
    private static final String PROPERTY_ENTRY_PDFPRODUCER_CANNOT_BE_REMOVED = "module.directory.pdfproducer.message.producer.config.entry.not.deleted";

    /**
    * {@inheritDoc}
    */
    public boolean canBeRemoved( String strId )
    {
        if ( StringUtils.isNotBlank( strId ) )
        {
            return !ConfigProducerHome.checkEntry( PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ),
                DirectoryUtils.convertStringToInt( strId ) );
        }

        return true;
    }

    /**
    * {@inheritDoc}
    */
    public String getRemovalRefusedMessage( String id, Locale locale )
    {
        return I18nService.getLocalizedString( PROPERTY_ENTRY_PDFPRODUCER_CANNOT_BE_REMOVED, locale );
    }
}
