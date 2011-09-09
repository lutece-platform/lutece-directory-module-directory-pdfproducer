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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * Allow to access ProducerConfig data
 *
 */
public class ConfigProducerDAO implements IConfigProducerDAO
{
    private static final String SQL_QUERY_INSERT_CONFIG_PRODUCER = "INSERT INTO directory_config_producer (id_config,name,id_entry_name_file,id_directory,config_type,text_file_name,type_config_file_name) VALUES ( ? , ? , ? , ? , ? , ? , ? );";
    private static final String SQL_QUERY_INSERT_CONFIG_ENTRY = "INSERT INTO directory_config_entry (id_config,id_entry) VALUES ( ? , ? );";
    private static final String SQL_QUERY_SELECT_MAX_ID = "SELECT max(id_config) FROM directory_config_producer";
    private static final String SQL_QUERY_SELECT_CONFIG = "SELECT id_config, name, id_entry_name_file, id_directory, config_type, text_file_name, type_config_file_name FROM directory_config_producer WHERE id_config = ? ;";
    private static final String SQL_QUERY_SELECT_CONFIG_BY_DIRECTORY = "SELECT id_config, name, id_entry_name_file, id_directory, config_type, text_file_name, type_config_file_name FROM directory_config_producer WHERE id_directory = ? AND config_type = ? ;";
    private static final String SQL_QUERY_SELECT_CONFIG_ENTRY = "SELECT id_entry FROM directory_config_entry WHERE id_config = ? ;";
    private static final String SQL_QUERY_DELETE_CONFIG_PRODUCER = "DELETE FROM directory_config_producer WHERE id_config = ? ";
    private static final String SQL_QUERY_DELETE_CONFIG_ENTRY = "DELETE FROM directory_config_entry WHERE id_config = ? ";
    private static final String SQL_QUERY_UPDATE_CONFIG_ENTRY = "UPDATE directory_config_producer SET name = ? , id_entry_name_file = ? , config_type = ? , text_file_name = ? , type_config_file_name = ? WHERE id_config = ? ;";
    private static final String SQL_QUERY_SELECT_BY_DIRECTORY = "SELECT id_config, name, id_entry_name_file, id_directory, config_type, text_file_name, type_config_file_name FROM directory_config_producer WHERE id_directory = ? ;";
    private static final String SQL_QUERY_DELETE_BY_DIRECTORY = "DELETE FROM directory_config_producer WHERE id_directory = ? ;";
    private static final String SQL_QUERY_SELECT_ENTRY = "SELECT id_config, id_entry FROM directory_config_entry WHERE id_entry = ? ;";
    private static final String PARAMETER_COPY_NAME = "module.directory.pdfproducer.create.producer.config.copy.name";
    private static final String SQL_QUERY_SELECT_CONFIG_DEFAULT = "SELECT id_config FROM directory_default_config WHERE id_directory = ? ;";
    private static final String SQL_QUERY_SAVE_CONFIG_DEFAULT = "INSERT INTO directory_default_config (id_config, id_directory) VALUES ( ? , ? ) ;";
    private static final String SQL_QUERY_UPDATE_CONFIG_DEFAULT = "UPDATE directory_default_config SET id_config = ? WHERE id_directory = ? ;";

    /**
    * {@inheritDoc}
    */
    public void addNewConfig( Plugin plugin, ConfigProducer configProducer, List<Integer> listIdEntry )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_ID, plugin );
        daoUtil.executeQuery(  );

        int nIdConfig = 1;

        while ( daoUtil.next(  ) )
        {
            nIdConfig = daoUtil.getInt( 1 ) + 1;
        }
        
        daoUtil.free(  );

        daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONFIG_PRODUCER, plugin );
        daoUtil.setInt( 1, nIdConfig );
        daoUtil.setString( 2, configProducer.getName(  ) );
        daoUtil.setInt( 3, configProducer.getIdEntryFileName(  ) );
        daoUtil.setInt( 4, configProducer.getIdDirectory(  ) );
        daoUtil.setString( 5, configProducer.getType(  ) );
        daoUtil.setString( 6, configProducer.getTextFileName(  ) );
        daoUtil.setString( 7, configProducer.getTypeConfigFileName(  ) );

        daoUtil.executeUpdate(  );
        
        daoUtil.free(  );

        if ( !listIdEntry.isEmpty(  ) )
        {
            for ( Integer idEntry : listIdEntry )
            {
                daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONFIG_ENTRY, plugin );
                daoUtil.setInt( 1, nIdConfig );
                daoUtil.setInt( 2, idEntry.intValue(  ) );
                daoUtil.executeUpdate(  );
                daoUtil.free(  );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    public ConfigProducer loadConfig( Plugin plugin, int nIdConfig )
    {
        ConfigProducer configProducer = new ConfigProducer(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONFIG, plugin );
        daoUtil.setInt( 1, nIdConfig );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            configProducer.setIdProducerConfig( daoUtil.getInt( 1 ) );
            configProducer.setName( daoUtil.getString( 2 ) );
            configProducer.setIdEntryFileName( daoUtil.getInt( 3 ) );
            configProducer.setIdDirectory( daoUtil.getInt( 4 ) );
            configProducer.setType( daoUtil.getString( 5 ) );
            configProducer.setTextFileName( daoUtil.getString( 6 ) );
            configProducer.setTypeConfigFileName( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return configProducer;
    }

    /**
     * {@inheritDoc}
     */
    public List<ConfigProducer> loadListProducerConfig( Plugin plugin, int nIdDirectory, String strConfigType )
    {
        List<ConfigProducer> listProducerConfig = new ArrayList<ConfigProducer>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONFIG_BY_DIRECTORY, plugin );
        daoUtil.setInt( 1, nIdDirectory );
        daoUtil.setString( 2, strConfigType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ConfigProducer producerConfig = new ConfigProducer(  );
            producerConfig.setIdProducerConfig( daoUtil.getInt( 1 ) );
            producerConfig.setName( daoUtil.getString( 2 ) );
            producerConfig.setIdEntryFileName( daoUtil.getInt( 3 ) );
            producerConfig.setIdDirectory( nIdDirectory );
            producerConfig.setType( daoUtil.getString( 5 ) );
            producerConfig.setTextFileName( daoUtil.getString( 6 ) );
            producerConfig.setTypeConfigFileName( daoUtil.getString( 7 ) );
            listProducerConfig.add( producerConfig );
        }

        daoUtil.free(  );

        return listProducerConfig;
    }

    /**
     * {@inheritDoc}
     */
    public List<Integer> loadListConfigEntry( Plugin plugin, int nIdConfig )
    {
        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONFIG_ENTRY, plugin );
        daoUtil.setInt( 1, nIdConfig );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listIdEntry.add( Integer.valueOf( daoUtil.getInt( 1 ) ) );
        }

        daoUtil.free(  );

        return listIdEntry;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteProducerConfig( Plugin plugin, int nIdConfigProducer )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_CONFIG_PRODUCER, plugin );
        daoUtil.setInt( 1, nIdConfigProducer );
        daoUtil.executeUpdate(  );

        daoUtil = new DAOUtil( SQL_QUERY_DELETE_CONFIG_ENTRY, plugin );
        daoUtil.setInt( 1, nIdConfigProducer );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void modifyProducerConfig( Plugin plugin, ConfigProducer configProducer, List<Integer> listIdEntry )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_CONFIG_ENTRY, plugin );
        daoUtil.setInt( 1, configProducer.getIdProducerConfig(  ) );
        daoUtil.executeUpdate(  );

        if ( !listIdEntry.isEmpty(  ) )
        {
            for ( Integer idEntry : listIdEntry )
            {
                daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONFIG_ENTRY, plugin );
                daoUtil.setInt( 1, configProducer.getIdProducerConfig(  ) );
                daoUtil.setInt( 2, idEntry.intValue(  ) );
                daoUtil.executeUpdate(  );
            }
        }

        daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONFIG_ENTRY, plugin );
        daoUtil.setString( 1, configProducer.getName(  ) );
        daoUtil.setInt( 2, configProducer.getIdEntryFileName(  ) );
        daoUtil.setString( 3, configProducer.getType(  ) );
        daoUtil.setString( 4, configProducer.getTextFileName(  ) );
        daoUtil.setString( 5, configProducer.getTypeConfigFileName(  ) );
        daoUtil.setInt( 6, configProducer.getIdProducerConfig(  ) );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void copyProducerConfig( Plugin plugin, int nIdConfig, Locale locale )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONFIG, plugin );
        daoUtil.setInt( 1, nIdConfig );
        daoUtil.executeQuery(  );

        List<Integer> listIdEntry = loadListConfigEntry( plugin, nIdConfig );

        while ( daoUtil.next(  ) )
        {
            ConfigProducer configProducer = new ConfigProducer(  );
            configProducer.setName( I18nService.getLocalizedString( PARAMETER_COPY_NAME, locale ) + " " +
                daoUtil.getString( 2 ) );
            configProducer.setIdEntryFileName( daoUtil.getInt( 3 ) );
            configProducer.setIdDirectory( daoUtil.getInt( 4 ) );
            configProducer.setType( daoUtil.getString( 5 ) );
            configProducer.setTextFileName( daoUtil.getString( 6 ) );
            configProducer.setTypeConfigFileName( daoUtil.getString( 7 ) );

            addNewConfig( plugin, configProducer, listIdEntry );
        }

        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void deleteByDirectory( Plugin plugin, int nIdDirectory )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DIRECTORY, plugin );
        daoUtil.setInt( 1, nIdDirectory );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DAOUtil daoUtil2 = new DAOUtil( SQL_QUERY_DELETE_CONFIG_ENTRY, plugin );
            daoUtil2.setInt( 1, daoUtil.getInt( 1 ) );
            daoUtil2.executeUpdate(  );
            daoUtil2.free(  );
        }

        daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_DIRECTORY, plugin );
        daoUtil.setInt( 1, nIdDirectory );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkEntry( Plugin plugin, int nIdEntry )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ENTRY, plugin );
        daoUtil.setInt( 1, nIdEntry );
        daoUtil.executeQuery(  );
        boolean bCheckEntry = daoUtil.next(  );
        daoUtil.free(  );
        return bCheckEntry;
    }
    
    /**
     * {@inheritDoc}
     */
    public int loadDefaultConfig( Plugin plugin, int nIdDirectory )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONFIG_DEFAULT, plugin );
        daoUtil.setInt( 1, nIdDirectory );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            int nIdConfig = daoUtil.getInt( 1 );
            daoUtil.free(  );

            return nIdConfig;
        }
        else
        {
            daoUtil.free(  );

            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createDefaultConfig( Plugin plugin, int nIdDirectory, int nIdConfig )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SAVE_CONFIG_DEFAULT, plugin );
        daoUtil.setInt( 1, nIdConfig );
        daoUtil.setInt( 2, nIdDirectory );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void updateDefaultConfig( Plugin plugin, int nIdDirectory, int nIdConfig )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONFIG_DEFAULT, plugin );
        daoUtil.setInt( 1, nIdConfig );
        daoUtil.setInt( 2, nIdDirectory );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
