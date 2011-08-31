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


/**
 * Configuration to generate PDF.
 * AdminUser can select different directory entry to make them appear in the PDF.
 *
 */
public class ConfigProducer
{
    private int _nIdProducerConfig;
    private String _strName;
    private int _nIdDirectory;
    private String _strType;

    /**
     * @return the _nIdProducerConfig
     */
    public int getIdProducerConfig(  )
    {
        return _nIdProducerConfig;
    }

    /**
     * @param nIdProducerConfig the _nIdProducerConfig to set
     */
    public void setIdProducerConfig( int nIdProducerConfig )
    {
        _nIdProducerConfig = nIdProducerConfig;
    }

    /**
     * @return the _strName
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * @param strName the _strName to set
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * @return the _nIdDirectory
     */
    public int getIdDirectory(  )
    {
        return _nIdDirectory;
    }

    /**
     * @param nIdDirectory the _nIdDirectory to set
     */
    public void setIdDirectory( int nIdDirectory )
    {
        _nIdDirectory = nIdDirectory;
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
}
