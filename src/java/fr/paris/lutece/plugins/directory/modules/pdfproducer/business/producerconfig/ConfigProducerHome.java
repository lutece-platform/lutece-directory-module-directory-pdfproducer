/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.plugins.directory.modules.pdfproducer.service.DirectoryPDFProducerPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Locale;


/**
 * ConfigProducerHome
 *
 */
public final class ConfigProducerHome
{
    private static IConfigProducerDAO _dao = (IConfigProducerDAO) SpringContextService.getPluginBean( DirectoryPDFProducerPlugin.PLUGIN_NAME,
            "configProducerDAO" );

    /**
     * Constructor
     */
    private ConfigProducerHome(  )
    {
    }

    /**
     * This method add a new config with different directory entry selected by AdminUser
     * @param plugin The plugin
     * @param configProducer configuration
     * @param listIdEntry The list of entry id that appear in configuration
     */
    public static void addNewConfig( Plugin plugin, ConfigProducer configProducer, List<Integer> listIdEntry )
    {
        _dao.addNewConfig( plugin, configProducer, listIdEntry );
    }

    /**
     * This method load a config
     * @param plugin plugin
     * @param nIdConfig config id
     * @return ConfigProducer
     */
    public static ConfigProducer loadConfig( Plugin plugin, int nIdConfig )
    {
        return _dao.loadConfig( plugin, nIdConfig );
    }

    /**
     * This method load a list of config by directory id and type
     * @param plugin The plugin
     * @param nIdDirectory The id of directory
     * @param strConfigType The type of configuration
     * @return The ProducerConfig list
     */
    public static List<ConfigProducer> loadListProducerConfig( Plugin plugin, int nIdDirectory, String strConfigType )
    {
        return _dao.loadListProducerConfig( plugin, nIdDirectory, strConfigType );
    }

    /**
     * This method load a list of id Entry by id config
     * @param plugin The plugin
     * @param nIdConfig The config id
     * @return The id entry list
     */
    public static List<Integer> loadListConfigEntry( Plugin plugin, int nIdConfig )
    {
        return _dao.loadListConfigEntry( plugin, nIdConfig );
    }

    /**
     * This method delete a config by id
     * @param plugin plugin
     * @param nIdConfigProducer id config producer
     */
    public static void deleteProducerConfig( Plugin plugin, int nIdConfigProducer )
    {
        _dao.deleteProducerConfig( plugin, nIdConfigProducer );
    }

    /**
     * This method modify a config
     * @param plugin plugin
     * @param configProducer configuration
     * @param listIdEntry list of id entry
     */
    public static void modifyProducerConfig( Plugin plugin, ConfigProducer configProducer, List<Integer> listIdEntry )
    {
        _dao.modifyProducerConfig( plugin, configProducer, listIdEntry );
    }

    /**
     * This method copy a config
     * @param plugin plugin
     * @param nIdConfig id configproduducer
     * @param locale locale
     */
    public static void copyProducerConfig( Plugin plugin, int nIdConfig, Locale locale )
    {
        _dao.copyProducerConfig( plugin, nIdConfig, locale );
    }

    /**
     * This method check if a config exists for a specific directory
     * @param plugin plugin
     * @param nIdDirectory id of directory
     */
    public static void deleteByDirectory( Plugin plugin, int nIdDirectory )
    {
        _dao.deleteByDirectory( plugin, nIdDirectory );
    }

    /**
     * This method check if an entry is used by a config
     * @param plugin plugin
     * @param nIdEntry id of entry
     * @return true an entry is used by a config otherwise false
     */
    public static boolean checkEntry( Plugin plugin, int nIdEntry )
    {
        return _dao.checkEntry( plugin, nIdEntry );
    }

    /**
     * This method loads a default config
     * @param plugin plugin
     * @param nIdDirectory id directory
     * @return id config
     */
    public static int loadDefaultConfig( Plugin plugin, int nIdDirectory )
    {
        return _dao.loadDefaultConfig( plugin, nIdDirectory );
    }

    /**
     * This method add default config
     * @param plugin plugin
     * @param nIdDirectory id directory
     * @param nIdConfig id config
     */
    public static void createDefaultConfig( Plugin plugin, int nIdDirectory, int nIdConfig )
    {
        _dao.createDefaultConfig( plugin, nIdDirectory, nIdConfig );
    }

    /**
     * This method update default config
     * @param plugin plugin
     * @param nIdDirectory id directory
     * @param nIdConfig id config
     */
    public static void updateDefaultConfig( Plugin plugin, int nIdDirectory, int nIdConfig )
    {
        _dao.updateDefaultConfig( plugin, nIdDirectory, nIdConfig );
    }
}
