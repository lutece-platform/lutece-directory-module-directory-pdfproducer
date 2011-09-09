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
package fr.paris.lutece.plugins.directory.modules.pdfproducer.web;

import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.business.producerconfig.ConfigProducer;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.business.producerconfig.DefaultConfigProducer;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.service.ConfigProducerService;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.service.DirectoryPDFProducerPlugin;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * PDFProducerJspBean
 *
 */
public class PDFProducerJspBean extends PluginAdminPageJspBean
{
    // Templates
    private static final String TEMPLATE_CREATE_CONFIG = "admin/plugins/directory/modules/pdfproducer/create_new_configproducer.html";
    private static final String TEMPLATE_MANAGE_CONFIG = "admin/plugins/directory/modules/pdfproducer/manage_configproducer.html";
    private static final String TEMPLATE_MANAGE_DEFAULT_CONFIG = "admin/plugins/directory/modules/pdfproducer/manage_default_configproducer.html";
    private static final String TEMPLATE_MODIFY_CONFIG = "admin/plugins/directory/modules/pdfproducer/modify_configproducer.html";

    //Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_CONFIG_PRODUCER = "id_config_producer";
    private static final String PARAMETER_CHECK_PAGE_CONFIG = "page_config";
    private static final String PARAMETER_CONFIG_ENTRY = "config_entry";
    private static final String PARAMETER_CREATECONFIG_SAVE = "save";
    private static final String PARAMETER_CREATECONFIG_CANCEL = "cancel";
    private static final String PARAMETER_CREATECONFIG_NAME = "name";
    private static final String PARAMETER_ID_ENTRY_FILE_NAME = "id_entry_filename";
    private static final String PARAMETER_ID_DEFAULT_CONFIG = "id_default_config";
    private static final String PARAMETER_TYPE_CONFIG_FILE_NAME = "type_config_file_name";
    private static final String PARAMETER_TEXT_FILE_NAME = "text_file_name";

    //Markers
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_CONFIG_LIST = "config_list";
    private static final String MARK_ID_DIRECTORY = "idDirectory";
    private static final String MARK_ID_CONFIG_PRODUCER = "idConfigProducer";
    private static final String MARK_ID_ENTRY_LIST = "id_entry_list";
    private static final String MARK_CONFIG_PRODUCER = "config_producer";
    private static final String MARK_CREATECONFIG_NAME = "config_name";
    private static final String MARK_LIST_ENTRIES_FILENAME = "list_entries_filename";
    private static final String MARK_POSITION_ENTRY_FILENAME = "position_entry_directory_filename";
    private static final String MARK_ID_DEFAULT_CONFIG_SAVED = "id_defaut_config_saved";
    private static final String MARK_TEXT_FILE_NAME = "mark_text_file_name";
    private static final String MARK_TYPE_CONFIG_FILE_NAME = "mark_type_config_file_name";

    // Properties
    private static final String PROPERTY_ID_ENTRIES_TYPE_ALLOWED = "directory.filter.entries.type.config.default";

    // JSP URL
    private static final String JSP_MANAGE_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/ManageConfigProducer.jsp";
    private static final String JSP_CREATE_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/CreateConfigProducer.jsp";
    private static final String JSP_CREATE_CONFIG_PRODUCER_BIS = "CreateConfigProducer.jsp";
    private static final String JSP_DO_DELETE_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/DoRemoveConfigProducer.jsp";
    private static final String JSP_MODIFY_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/ModifyConfigProducer.jsp";
    private static final String JSP_MODIFY_CONFIG_PRODUCER_BIS = "ModifyConfigProducer.jsp";

    // Messages (I18n keys)
    private static final String MESSAGE_ADD_NEW_CONFIG = "module.directory.pdfproducer.message.producer.config.confirm.create";
    private static final String MESSAGE_CHOICE_DEFAUT_CONFIG = "module.directory.pdfproducer.message.choice.default.config";
    private static final String MESSAGE_CANCEL_CREATE = "module.directory.pdfproducer.message.producer.config.cancel.create";
    private static final String MESSAGE_NAME_CONFIG_MISSED = "module.directory.pdfproducer.message.producer.config.name.missed";
    private static final String MESSAGE_ENTRY_FILE_NAME_MISSED = "module.directory.pdfproducer.message.producer.config.entry.file.name.missed";
    private static final String MESSAGE_DELETE_CONFIG = "module.directory.pdfproducer.message.producer.config.delete";
    private static final String MESSAGE_DELETED_CONFIG = "module.directory.pdfproducer.message.producer.config.deleted";

    //Constant
    private static final String DEFAULT_VALUE = "-1";
    private static final String COMMA = ",";

    // Type Config
    private static final String TYPE_CONFIG_PDF = "PDF";
    private static final ConfigProducerService _manageConfigProducerService = (ConfigProducerService) SpringContextService.getPluginBean( DirectoryPDFProducerPlugin.PLUGIN_NAME,
            "directory-pdfproducer.manageConfigProducer" );

    /**
     * Display the page to manage configuration
     * @param request request
     * @return configuration html page
     */
    public String manageConfigProducer( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );

        List<ConfigProducer> listConfigProducer = _manageConfigProducerService.loadListProducerConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdDirectory ), TYPE_CONFIG_PDF );

        model.put( MARK_ID_DIRECTORY, strIdDirectory );
        model.put( MARK_CONFIG_LIST, listConfigProducer );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CONFIG, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Display the page to create a new configuration
     * @param request request
     * @return create html page
     */
    public String createConfigProducer( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strNameConfig = request.getParameter( PARAMETER_CREATECONFIG_NAME );
        String[] listIdEntry = request.getParameterValues( PARAMETER_CONFIG_ENTRY );
        String strIdEntryFileName = request.getParameter( PARAMETER_ID_ENTRY_FILE_NAME );

        String strTypeConfigFileName = request.getParameter( PARAMETER_TYPE_CONFIG_FILE_NAME );
        String strTextFileName = request.getParameter( PARAMETER_TEXT_FILE_NAME );

        List<IEntry> listEntry = DirectoryUtils.getFormEntries( DirectoryUtils.convertStringToInt( strIdDirectory ),
                getPlugin(  ), getUser(  ) );

        model.put( MARK_ENTRY_LIST, listEntry );
        model.put( MARK_ID_ENTRY_LIST, listIdEntry );
        model.put( MARK_ID_DIRECTORY, strIdDirectory );

        if ( strTextFileName != null )
        {
            model.put( MARK_TEXT_FILE_NAME, strTextFileName );
        }

        if ( StringUtils.isNotBlank( strTypeConfigFileName ) )
        {
            model.put( MARK_TYPE_CONFIG_FILE_NAME, strTypeConfigFileName );
        }

        if ( StringUtils.isNotBlank( strIdEntryFileName ) )
        {
            model.put( MARK_POSITION_ENTRY_FILENAME, strIdEntryFileName );
        }

        model.put( MARK_LIST_ENTRIES_FILENAME,
            getListEntriesUrl( DirectoryUtils.convertStringToInt( strIdDirectory ), request ) );

        if ( StringUtils.isNotBlank( strNameConfig ) )
        {
            model.put( MARK_CREATECONFIG_NAME, strNameConfig );
        }

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_CREATE_CONFIG, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Create a new configuration
     * @param request request
     * @return message to confirm the creation or not
     */
    public String doCreateConfigProducer( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strNameConfig = request.getParameter( PARAMETER_CREATECONFIG_NAME );
        String strIdEntryFileName = request.getParameter( PARAMETER_ID_ENTRY_FILE_NAME );
        String strTypeConfigFileName = request.getParameter( PARAMETER_TYPE_CONFIG_FILE_NAME );
        String strTextFileName = request.getParameter( PARAMETER_TEXT_FILE_NAME );

        String[] listStrIdEntry = request.getParameterValues( PARAMETER_CONFIG_ENTRY );
        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        if ( listStrIdEntry != null )
        {
            for ( int i = 0; i < listStrIdEntry.length; i++ )
            {
                if ( StringUtils.isNotBlank( listStrIdEntry[i] ) && StringUtils.isNumeric( listStrIdEntry[i] ) )
                {
                    listIdEntry.add( Integer.valueOf( listStrIdEntry[i] ) );
                }
            }
        }

        checkEntryGroup( listIdEntry );

        UrlItem url = new UrlItem( JSP_CREATE_CONFIG_PRODUCER );

        if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_SAVE ) )
        {
            url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );
            url.addParameter( PARAMETER_TYPE_CONFIG_FILE_NAME, strTypeConfigFileName );
            url.addParameter( PARAMETER_TEXT_FILE_NAME, strTextFileName );

            if ( !listIdEntry.isEmpty(  ) )
            {
                for ( Integer idEntry : listIdEntry )
                {
                    url.addParameter( PARAMETER_CONFIG_ENTRY, String.valueOf( idEntry ) );
                }
            }

            if ( StringUtils.isBlank( strNameConfig ) )
            {
                url.addParameter( PARAMETER_ID_ENTRY_FILE_NAME, strIdEntryFileName );

                return AdminMessageService.getMessageUrl( request, MESSAGE_NAME_CONFIG_MISSED, url.getUrl(  ),
                    AdminMessage.TYPE_ERROR );
            }
            else if ( StringUtils.isBlank( strIdEntryFileName ) && strIdEntryFileName.equals( DEFAULT_VALUE ) )
            {
                url.addParameter( PARAMETER_CREATECONFIG_NAME, strIdEntryFileName );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ENTRY_FILE_NAME_MISSED, url.getUrl(  ),
                    AdminMessage.TYPE_ERROR );
            }
            else
            {
                url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );
                url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

                ConfigProducer configProducer = new ConfigProducer(  );
                configProducer.setName( strNameConfig );
                configProducer.setIdEntryFileName( DirectoryUtils.convertStringToInt( strIdEntryFileName ) );
                configProducer.setIdDirectory( DirectoryUtils.convertStringToInt( strIdDirectory ) );
                configProducer.setType( TYPE_CONFIG_PDF );
                configProducer.setTextFileName( strTextFileName );
                configProducer.setTypeConfigFileName( strTypeConfigFileName );

                _manageConfigProducerService.addNewConfig( getPlugin(  ), configProducer, listIdEntry );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ADD_NEW_CONFIG, url.getUrl(  ),
                    AdminMessage.TYPE_INFO );
            }
        }
        else if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_CANCEL ) )
        {
            url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );
            url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANCEL_CREATE, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else
        {
            url = new UrlItem( JSP_CREATE_CONFIG_PRODUCER_BIS );

            return doCheckAll( url, strIdDirectory, listIdEntry, strNameConfig, strIdEntryFileName, strTextFileName,
                strTypeConfigFileName );
        }
    }

    /**
     * Message to confirm to delete a configuration
     * @param request request
     * @return admin message
     */
    public String deleteConfigProducer( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdConfigProducer = request.getParameter( PARAMETER_ID_CONFIG_PRODUCER );

        UrlItem url = new UrlItem( JSP_DO_DELETE_CONFIG_PRODUCER );
        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );
        url.addParameter( PARAMETER_ID_CONFIG_PRODUCER, strIdConfigProducer );

        return AdminMessageService.getMessageUrl( request, MESSAGE_DELETE_CONFIG, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete a configuration
     * @param request request
     * @return a message to confirm and redirect to manage page
     */
    public String doDeleteConfigProducer( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdConfigProducer = request.getParameter( PARAMETER_ID_CONFIG_PRODUCER );

        UrlItem url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );
        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

        _manageConfigProducerService.deleteProducerConfig( getPlugin(  ),
            DirectoryUtils.convertStringToInt( strIdConfigProducer ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_DELETED_CONFIG, url.getUrl(  ),
            AdminMessage.TYPE_INFO );
    }

    /**
     * Display the page to modify a configuration
     * @param request request
     * @return modify html page
     */
    public String modifyConfigProducer( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strNameConfig = request.getParameter( PARAMETER_CREATECONFIG_NAME );
        String strIdConfigProducer = request.getParameter( PARAMETER_ID_CONFIG_PRODUCER );
        String strCheckPageConfig = request.getParameter( PARAMETER_CHECK_PAGE_CONFIG );
        String[] listStrIdEntry = request.getParameterValues( PARAMETER_CONFIG_ENTRY );
        String strIdEntryFileName = request.getParameter( PARAMETER_ID_ENTRY_FILE_NAME );

        String strTypeConfigFileName = request.getParameter( PARAMETER_TYPE_CONFIG_FILE_NAME );
        String strTextFileName = request.getParameter( PARAMETER_TEXT_FILE_NAME );

        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        if ( listStrIdEntry != null )
        {
            for ( int i = 0; i < listStrIdEntry.length; i++ )
            {
                if ( StringUtils.isNotBlank( listStrIdEntry[i] ) && StringUtils.isNumeric( listStrIdEntry[i] ) )
                {
                    listIdEntry.add( Integer.valueOf( listStrIdEntry[i] ) );
                }
            }
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        _manageConfigProducerService.loadListConfigEntry( getPlugin(  ),
            DirectoryUtils.convertStringToInt( strIdConfigProducer ) );

        List<IEntry> listEntry = DirectoryUtils.getFormEntries( DirectoryUtils.convertStringToInt( strIdDirectory ),
                getPlugin(  ), getUser(  ) );

        ConfigProducer configProducer = _manageConfigProducerService.loadConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdConfigProducer ) );

        model.put( MARK_ENTRY_LIST, listEntry );

        if ( StringUtils.isNotBlank( strCheckPageConfig ) )
        {
            model.put( MARK_ID_ENTRY_LIST, listIdEntry );
        }
        else
        {
            model.put( MARK_ID_ENTRY_LIST,
                _manageConfigProducerService.loadListConfigEntry( getPlugin(  ),
                    DirectoryUtils.convertStringToInt( strIdConfigProducer ) ) );
        }

        if ( strTextFileName == null )
        {
            model.put( MARK_TEXT_FILE_NAME, configProducer.getTextFileName(  ) );
        }
        else
        {
            model.put( MARK_TEXT_FILE_NAME, strTextFileName );
        }

        if ( StringUtils.isBlank( strTypeConfigFileName ) )
        {
            model.put( MARK_TYPE_CONFIG_FILE_NAME, configProducer.getTypeConfigFileName(  ) );
        }
        else
        {
            model.put( MARK_TYPE_CONFIG_FILE_NAME, strTypeConfigFileName );
        }

        model.put( MARK_ID_DIRECTORY, strIdDirectory );
        model.put( MARK_ID_CONFIG_PRODUCER, strIdConfigProducer );
        model.put( MARK_CONFIG_PRODUCER, configProducer );

        if ( StringUtils.isNotBlank( strIdEntryFileName ) )
        {
            model.put( MARK_POSITION_ENTRY_FILENAME, strIdEntryFileName );
        }
        else
        {
            model.put( MARK_POSITION_ENTRY_FILENAME, String.valueOf( configProducer.getIdEntryFileName(  ) ) );
        }

        model.put( MARK_LIST_ENTRIES_FILENAME,
            getListEntriesUrl( DirectoryUtils.convertStringToInt( strIdDirectory ), request ) );

        if ( StringUtils.isNotBlank( strNameConfig ) )
        {
            model.put( MARK_CREATECONFIG_NAME, strNameConfig );
        }

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MODIFY_CONFIG, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Modify a configuration
     * @param request request
     * @return a message to confirm and redirect to manage page
     */
    public String doModifyConfigProducer( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdConfigProducer = request.getParameter( PARAMETER_ID_CONFIG_PRODUCER );
        String strCheckPageConfig = request.getParameter( PARAMETER_CHECK_PAGE_CONFIG );
        String[] listStrIdEntry = request.getParameterValues( PARAMETER_CONFIG_ENTRY );
        String strIdEntryFileName = request.getParameter( PARAMETER_ID_ENTRY_FILE_NAME );
        String strNameConfig = request.getParameter( PARAMETER_CREATECONFIG_NAME );

        String strTypeConfigFileName = request.getParameter( PARAMETER_TYPE_CONFIG_FILE_NAME );
        String strTextFileName = request.getParameter( PARAMETER_TEXT_FILE_NAME );

        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        if ( listStrIdEntry != null )
        {
            for ( int i = 0; i < listStrIdEntry.length; i++ )
            {
                if ( StringUtils.isNotBlank( listStrIdEntry[i] ) && StringUtils.isNumeric( listStrIdEntry[i] ) )
                {
                    listIdEntry.add( Integer.valueOf( listStrIdEntry[i] ) );
                }
            }
        }

        checkEntryGroup( listIdEntry );

        UrlItem url = new UrlItem( JSP_MODIFY_CONFIG_PRODUCER );
        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );
        url.addParameter( PARAMETER_TYPE_CONFIG_FILE_NAME, strTypeConfigFileName );
        url.addParameter( PARAMETER_TEXT_FILE_NAME, strTextFileName );

        if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_SAVE ) )
        {
            url.addParameter( PARAMETER_ID_CONFIG_PRODUCER, strIdConfigProducer );
            url.addParameter( PARAMETER_CHECK_PAGE_CONFIG, strCheckPageConfig );

            if ( !listIdEntry.isEmpty(  ) )
            {
                for ( Integer idEntry : listIdEntry )
                {
                    url.addParameter( PARAMETER_CONFIG_ENTRY, String.valueOf( idEntry ) );
                }
            }

            if ( StringUtils.isBlank( strNameConfig ) )
            {
                url.addParameter( PARAMETER_ID_ENTRY_FILE_NAME, strIdEntryFileName );

                return AdminMessageService.getMessageUrl( request, MESSAGE_NAME_CONFIG_MISSED, url.getUrl(  ),
                    AdminMessage.TYPE_ERROR );
            }
            else if ( StringUtils.isBlank( strIdEntryFileName ) && strIdEntryFileName.equals( DEFAULT_VALUE ) )
            {
                url.addParameter( PARAMETER_CREATECONFIG_NAME, strIdEntryFileName );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ENTRY_FILE_NAME_MISSED, url.getUrl(  ),
                    AdminMessage.TYPE_ERROR );
            }
            else
            {
                url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );
                url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

                ConfigProducer configProducer = new ConfigProducer(  );
                configProducer.setName( strNameConfig );
                configProducer.setIdProducerConfig( DirectoryUtils.convertStringToInt( strIdConfigProducer ) );
                configProducer.setIdEntryFileName( DirectoryUtils.convertStringToInt( strIdEntryFileName ) );
                configProducer.setIdDirectory( DirectoryUtils.convertStringToInt( strIdDirectory ) );
                configProducer.setType( TYPE_CONFIG_PDF );
                configProducer.setTextFileName( strTextFileName );
                configProducer.setTypeConfigFileName( strTypeConfigFileName );

                _manageConfigProducerService.modifyProducerConfig( getPlugin(  ), configProducer, listIdEntry );
            }

            return AdminMessageService.getMessageUrl( request, MESSAGE_ADD_NEW_CONFIG, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_CANCEL ) )
        {
            url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );
            url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANCEL_CREATE, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else
        {
            url = new UrlItem( JSP_MODIFY_CONFIG_PRODUCER_BIS );
            url.addParameter( PARAMETER_CHECK_PAGE_CONFIG, strCheckPageConfig );
            url.addParameter( PARAMETER_ID_CONFIG_PRODUCER, strIdConfigProducer );

            return doCheckAll( url, strIdDirectory, listIdEntry, strNameConfig, strIdEntryFileName, strTextFileName,
                strTypeConfigFileName );
        }
    }

    /**
     * Copy a configuration
     * @param request request
     * @return manage page
     */
    public String doCopyConfigProducer( HttpServletRequest request )
    {
        String strIdConfigProducer = request.getParameter( PARAMETER_ID_CONFIG_PRODUCER );
        _manageConfigProducerService.copyProducerConfig( getPlugin(  ),
            DirectoryUtils.convertStringToInt( strIdConfigProducer ), request.getLocale(  ) );

        return manageConfigProducer( request );
    }

    /**
     * This method delete an id group entry if it have no child in config list
     * @param listIdEntry list of id entry
     */
    private void checkEntryGroup( List<Integer> listIdEntry )
    {
        List<Integer> listIdEntryToDelete = new ArrayList<Integer>(  );
        boolean bNoChildEntryGroup = false;

        for ( Integer idEntry : listIdEntry )
        {
            IEntry entry = EntryHome.findByPrimaryKey( idEntry.intValue(  ), getPlugin(  ) );

            if ( entry.getEntryType(  ).getGroup(  ) && ( entry.getChildren(  ) != null ) )
            {
                for ( IEntry child : entry.getChildren(  ) )
                {
                    if ( listIdEntry.contains( child.getIdEntry(  ) ) )
                    {
                        bNoChildEntryGroup = true;
                    }
                }

                if ( !bNoChildEntryGroup )
                {
                    listIdEntryToDelete.add( idEntry );
                }
            }

            bNoChildEntryGroup = false;
        }

        if ( !listIdEntryToDelete.isEmpty(  ) )
        {
            listIdEntry.removeAll( listIdEntryToDelete );
        }
    }

    /**
     * Method to check all checkbox
     * @param url url to redirect
     * @param strIdDirectory id directory
     * @param listIdEntry list of id Entry
     * @param strNameconfig name of config
     * @param strIdEntryFileName id entry file name
     * @param strTextFileName text file name
     * @param strTypeConfigFileName type of configuration for file name
     * @return final url with params
     */
    private String doCheckAll( UrlItem url, String strIdDirectory, List<Integer> listIdEntry, String strNameconfig,
        String strIdEntryFileName, String strTextFileName, String strTypeConfigFileName )
    {
        List<IEntry> listEntry = DirectoryUtils.getFormEntries( DirectoryUtils.convertStringToInt( strIdDirectory ),
                getPlugin(  ), getUser(  ) );

        for ( IEntry entry : listEntry )
        {
            if ( entry.getEntryType(  ).getGroup(  ) )
            {
                if ( entry.getChildren(  ) != null )
                {
                    for ( IEntry child : entry.getChildren(  ) )
                    {
                        if ( !listIdEntry.contains( child.getIdEntry(  ) ) )
                        {
                            url.addParameter( PARAMETER_CONFIG_ENTRY, String.valueOf( child.getIdEntry(  ) ) );
                        }
                    }
                }
            }
            else
            {
                if ( !listIdEntry.contains( entry.getIdEntry(  ) ) )
                {
                    url.addParameter( PARAMETER_CONFIG_ENTRY, String.valueOf( entry.getIdEntry(  ) ) );
                }
            }
        }

        url.addParameter( PARAMETER_TYPE_CONFIG_FILE_NAME, strTypeConfigFileName );
        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );
        url.addParameter( PARAMETER_TEXT_FILE_NAME, strTextFileName );

        if ( StringUtils.isNotBlank( strNameconfig ) )
        {
            url.addParameter( PARAMETER_CREATECONFIG_NAME, strNameconfig );
        }

        if ( StringUtils.isNotBlank( strIdEntryFileName ) && !strIdEntryFileName.equals( DEFAULT_VALUE ) )
        {
            url.addParameter( PARAMETER_ID_ENTRY_FILE_NAME, strIdEntryFileName );
        }

        return url.getUrl(  );
    }

    /**
     * Method to get directory entries list
     * @param nIdDirectory id directory
     * @param request request
     * @return ReferenceList entries list
     */
    private static ReferenceList getListEntriesUrl( int nIdDirectory, HttpServletRequest request )
    {
        if ( nIdDirectory != -1 )
        {
            List<Integer> listIdEntriesTypeAllowed = fillListEntryTypes( PROPERTY_ID_ENTRIES_TYPE_ALLOWED );

            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
            List<IEntry> listEntries = DirectoryUtils.getFormEntries( nIdDirectory, pluginDirectory,
                    AdminUserService.getAdminUser( request ) );
            ReferenceList referenceList = new ReferenceList(  );

            for ( IEntry entry : listEntries )
            {
                if ( entry.getEntryType(  ).getGroup(  ) )
                {
                    if ( entry.getChildren(  ) != null )
                    {
                        for ( IEntry child : entry.getChildren(  ) )
                        {
                            if ( listIdEntriesTypeAllowed.contains( child.getEntryType(  ).getIdType(  ) ) )
                            {
                                ReferenceItem referenceItem = new ReferenceItem(  );
                                referenceItem.setCode( String.valueOf( child.getIdEntry(  ) ) );
                                referenceItem.setName( child.getTitle(  ) );
                                referenceList.add( referenceItem );
                            }
                        }
                    }
                }
                else
                {
                    if ( listIdEntriesTypeAllowed.contains( entry.getEntryType(  ).getIdType(  ) ) )
                    {
                        ReferenceItem referenceItem = new ReferenceItem(  );
                        referenceItem.setCode( String.valueOf( entry.getIdEntry(  ) ) );
                        referenceItem.setName( entry.getTitle(  ) );
                        referenceList.add( referenceItem );
                    }
                }
            }

            return referenceList;
        }
        else
        {
            return null;
        }
    }

    /**
     * This method displays the html page to choice the default configuration
     * @param request request
     * @return html page to manage advanced parameters
     */
    public String manageAdvancedParameters( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );

        int nIdDefaultConfig = _manageConfigProducerService.loadDefaultConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdDirectory ) );

        List<ConfigProducer> listConfigProducer = _manageConfigProducerService.loadListProducerConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdDirectory ), TYPE_CONFIG_PDF );

        ReferenceList referenceList = new ReferenceList(  );

        DefaultConfigProducer defaultConfigProducer = new DefaultConfigProducer(  );
        ReferenceItem referenceDefaultItem = new ReferenceItem(  );
        referenceDefaultItem.setName( defaultConfigProducer.getName( getLocale(  ) ) );
        referenceDefaultItem.setCode( Integer.toString( defaultConfigProducer.getIdProducerConfig(  ) ) );
        referenceList.add( referenceDefaultItem );

        for ( ConfigProducer configProducer : listConfigProducer )
        {
            ReferenceItem referenceItem = new ReferenceItem(  );
            referenceItem.setCode( Integer.toString( configProducer.getIdProducerConfig(  ) ) );
            referenceItem.setName( configProducer.getName(  ) );
            referenceList.add( referenceItem );
        }

        model.put( MARK_ID_DIRECTORY, strIdDirectory );
        model.put( MARK_CONFIG_LIST, referenceList );

        if ( nIdDefaultConfig == 0 )
        {
            model.put( MARK_ID_DEFAULT_CONFIG_SAVED, DirectoryUtils.convertStringToInt( DEFAULT_VALUE ) );
        }
        else
        {
            model.put( MARK_ID_DEFAULT_CONFIG_SAVED, nIdDefaultConfig );
        }

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DEFAULT_CONFIG, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Save default config
     * @param request request
     * @return confirm page
     */
    public String doSaveAdvancedParameters( HttpServletRequest request )
    {
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );
        String strIdDefaultConfig = request.getParameter( PARAMETER_ID_DEFAULT_CONFIG );

        int nIdDefaultConfig = _manageConfigProducerService.loadDefaultConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdDirectory ) );

        if ( nIdDefaultConfig == 0 )
        {
            _manageConfigProducerService.createDefaultConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdDirectory ),
                DirectoryUtils.convertStringToInt( strIdDefaultConfig ) );
        }
        else
        {
            _manageConfigProducerService.updateDefaultConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdDirectory ),
                DirectoryUtils.convertStringToInt( strIdDefaultConfig ) );
        }

        UrlItem url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );
        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CHOICE_DEFAUT_CONFIG, url.getUrl(  ),
            AdminMessage.TYPE_INFO );
    }

    /**
     * Fill the list of entry types
     * @param strPropertyEntryTypes the property containing the entry types
     * @return a list of integer
     */
    private static List<Integer> fillListEntryTypes( String strPropertyEntryTypes )
    {
        List<Integer> listEntryTypes = new ArrayList<Integer>(  );
        String strEntryTypes = AppPropertiesService.getProperty( strPropertyEntryTypes );

        if ( StringUtils.isNotBlank( strEntryTypes ) )
        {
            String[] listAcceptEntryTypesForIdDemand = strEntryTypes.split( COMMA );

            for ( String strAcceptEntryType : listAcceptEntryTypesForIdDemand )
            {
                if ( StringUtils.isNotBlank( strAcceptEntryType ) && StringUtils.isNumeric( strAcceptEntryType ) )
                {
                    int nAcceptedEntryType = Integer.parseInt( strAcceptEntryType );
                    listEntryTypes.add( nAcceptedEntryType );
                }
            }
        }

        return listEntryTypes;
    }
}
