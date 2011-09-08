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

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;
import java.util.Locale;


/**
 * IConfigProducerDAO
 *
 */
public interface IConfigProducerDAO
{
    /**
     * This method add a new config with different directory entry selected by AdminUser
     * @param plugin The plugin
     * @param strConfigName The name of configuration
     * @param nIdDirectory The id of directory
     * @param strConfigType The type of producer that uses this configuration
     * @param listIdEntry The list of entry id that appear in configuration
     */
    void addNewConfig( Plugin plugin, String strConfigName, int nIdEntryFileName, int nIdDirectory,
        String strConfigType, String strTextFileName, String strTypeConfigFileName, List<Integer> listIdEntry );

    /**
     * This method load a config
     * @param plugin plugin
     * @param nIdConfig config id
     * @return a ConfigProducer
     */
    ConfigProducer loadConfig( Plugin plugin, int nIdConfig );

    /**
     *  This method load a list of config by directory id and type
     * @param plugin The plugin
     * @param nIdDirectory The id of directory
     * @param strConfigType type of config
     * @return The ProducerConfig list
     */
    List<ConfigProducer> loadListProducerConfig( Plugin plugin, int nIdDirectory, String strConfigType );

    /**
     * This method load a list of id Entry by id config
     * @param plugin The plugin
     * @param nIdConfig The config id
     * @return The id entry list
     */
    List<Integer> loadListConfigEntry( Plugin plugin, int nIdConfig );

    /**
     * This method delete a config by id
     * @param plugin plugin
     * @param nIdConfigProducer id config producer
     */
    void deleteProducerConfig( Plugin plugin, int nIdConfigProducer );

    /**
     * This method modify a config
     * @param plugin plugin
     * @param strConfigName a new name
     * @param nIdConfigProducer id configproduducer
     * @param strConfigType config type
     * @param listIdEntry list of id entry
     */
    void modifyProducerConfig( Plugin plugin, String strConfigName, int nIdEntryFileName, int nIdConfigProducer,
        String strConfigType, String strTextFileName, String strTypeConfigFileName, List<Integer> listIdEntry );

    /**
     * This method copy a config
     * @param plugin plugin
     * @param nIdConfig id configproduducer
     * @param locale locale
     */
    void copyProducerConfig( Plugin plugin, int nIdConfig, Locale locale );

    /**
     * This method delete all config by id directory
     * @param plugin plugin
     * @param nIdDirectory id of directory
     */
    void deleteByDirectory( Plugin plugin, int nIdDirectory );

    /**
     * This method check if an entry is used by a config
     * @param plugin plugin
     * @param nIdEntry id of entry
     * @return true an entry is used by a config otherwise false
     */
    boolean checkEntry( Plugin plugin, int nIdEntry );

    /**
     * This method add new actions for directory record
     * @param plugin plugin
     */
    void addActionsDirectoryRecord( Plugin plugin );

    /**
     * This method loads a default config
     * @param plugin plugin
     * @param nIdDirectory id directory
     * @return id config
     */
    int loadDefaultConfig( Plugin plugin, int nIdDirectory );

    /**
     * This method add default config
     * @param plugin plugin
     * @param nIdDirectory id directory
     * @param nIdConfig id config
     */
    void createDefaultConfig( Plugin plugin, int nIdDirectory, int nIdConfig );

    /**
     * This method update default config
     * @param plugin plugin
     * @param nIdDirectory id directory
     * @param nIdConfig id config
     */
    void updateDefaultConfig( Plugin plugin, int nIdDirectory, int nIdConfig );
}
