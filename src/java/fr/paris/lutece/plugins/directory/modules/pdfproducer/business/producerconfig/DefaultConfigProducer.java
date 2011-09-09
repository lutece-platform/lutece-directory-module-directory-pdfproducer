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
package fr.paris.lutece.plugins.directory.modules.pdfproducer.business.producerconfig;

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;


/**
 * DefaultConfigProducer
 *
 */
public class DefaultConfigProducer implements IConfigProducer
{
    private static final String PROPERTY_DEFAULT_CONFIG_NAME = "module.directory.pdfproducer.create.producer.default.config.name";
    private static final String DEFAULT_TYPE_FILE_NAME = "default";
    private String _strType;

    /**
     * @return the _nIdProducerConfig
     */
    public int getIdProducerConfig(  )
    {
        return -1;
    }

    /**
     * @param nIdProducerConfig the _nIdProducerConfig to set
     */
    public void setIdProducerConfig( int nIdProducerConfig )
    {
    }

    /**
     * @return the _strName
     */
    public String getName(  )
    {
        return null;
    }

    /**
     * @param locale locale
     * @return the _strName
     */
    public String getName( Locale locale )
    {
        return I18nService.getLocalizedString( PROPERTY_DEFAULT_CONFIG_NAME, locale );
    }

    /**
     * @param nIdEntryFileName the _nIdEntryFileName to set
     */
    public void setIdEntryFileName( int nIdEntryFileName )
    {
    }

    /**
     * @return the _nIdEntryFileName
     */
    public int getIdEntryFileName(  )
    {
        return -1;
    }

    /**
     * @return the FileName
     */
    public int getFileName(  )
    {
        return -1;
    }

    /**
     * @param strName the _strName to set
     */
    public void setName( String strName )
    {
    }

    /**
     * @return the _nIdDirectory
     */
    public int getIdDirectory(  )
    {
        return -1;
    }

    /**
     * @param nIdDirectory the _nIdDirectory to set
     */
    public void setIdDirectory( int nIdDirectory )
    {
    }

    /**
     * @return the _strType
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * @param strType the _strType to set
     */
    public void setType( String strType )
    {
        _strType = strType;
    }

    /**
     * @return the _strTypeConfigFileName
     */
    public String getTypeConfigFileName(  )
    {
        return DEFAULT_TYPE_FILE_NAME;
    }

    /**
     * @param strTypeConfigFileName the _strTypeConfigFileName to set
     */
    public void setTypeConfigFileName( String strTypeConfigFileName )
    {
    }

    /**
     * @return the _strTextFileName
     */
    public String getTextFileName(  )
    {
        return null;
    }

    /**
     * @param strTextFileName the _strTextFileName to set
     */
    public void setTextFileName( String strTextFileName )
    {
    }
}
