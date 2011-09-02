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
import fr.paris.lutece.plugins.directory.modules.pdfproducer.service.ConfigProducerService;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.service.DirectoryPDFProducerPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ReferenceItem;
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
    private static final String TEMPLATE_MODIFY_CONFIG = "admin/plugins/directory/modules/pdfproducer/modify_configproducer.html";

    //Parameters
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_CONFIG_PRODUCER = "id_config_producer";
    private static final String PARAMETER_CHECK_PAGE_CONFIG = "page_config";
    private static final String PARAMETER_CONFIG_ENTRY = "config_entry";
    private static final String PARAMETER_CREATECONFIG_SAVE = "save";
    private static final String PARAMETER_CREATECONFIG_CANCEL = "cancel";
    private static final String PARAMETER_CREATECONFIG_NAME = "name";

    //Markers
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_CONFIG_LIST = "config_list";
    private static final String MARK_ID_DIRECTORY = "idDirectory";
    private static final String MARK_ID_CONFIG_PRODUCER = "idConfigProducer";
    private static final String MARK_ID_ENTRY_LIST = "id_entry_list";
    private static final String MARK_CONFIG_PRODUCER = "config_producer";
    private static final String MARK_CREATECONFIG_NAME = "config_name";

    // JSP URL
    private static final String JSP_MANAGE_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/ManageConfigProducer.jsp";
    private static final String JSP_CREATE_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/CreateConfigProducer.jsp";
    private static final String JSP_CREATE_CONFIG_PRODUCER_BIS = "CreateConfigProducer.jsp";
    private static final String JSP_DO_DELETE_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/DoRemoveConfigProducer.jsp";
    private static final String JSP_MODIFY_CONFIG_PRODUCER = "jsp/admin/plugins/directory/modules/pdfproducer/ModifyConfigProducer.jsp";
    private static final String JSP_MODIFY_CONFIG_PRODUCER_BIS = "ModifyConfigProducer.jsp";

    // Messages (I18n keys)
    private static final String MESSAGE_ADD_NEW_CONFIG = "module.directory.pdfproducer.message.producer.config.confirm.create";
    private static final String MESSAGE_CANCEL_CREATE = "module.directory.pdfproducer.message.producer.config.cancel.create";
    private static final String MESSAGE_NAME_CONFIG_MISSED = "module.directory.pdfproducer.message.producer.config.name.missed";
    private static final String MESSAGE_DELETE_CONFIG = "module.directory.pdfproducer.message.producer.config.delete";
    private static final String MESSAGE_DELETED_CONFIG = "module.directory.pdfproducer.message.producer.config.deleted";

    // Type Config
    private static final String TYPE_CONFIG_PDF = "PDF";

    /**
     * return the service to manage configproducer
     * @return manageConfigProducerService
     */
    private static ConfigProducerService getDirectoryManageZipBasketService(  )
    {
        ConfigProducerService manageConfigProducerService = (ConfigProducerService) SpringContextService.getPluginBean( DirectoryPDFProducerPlugin.PLUGIN_NAME,
                "directory-pdfproducer.manageConfigProducer" );

        return manageConfigProducerService;
    }

    /**
     * Display the page to manage configuration
     * @param request request
     * @return configuration html page
     */
    public String manageConfigProducer( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        String strIdDirectory = request.getParameter( PARAMETER_ID_DIRECTORY );

        ConfigProducerService configProducerService = getDirectoryManageZipBasketService(  );

        List<ConfigProducer> listConfigProducer = configProducerService.loadListProducerConfig( getPlugin(  ),
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

        List<IEntry> listEntry = DirectoryUtils.getFormEntries( DirectoryUtils.convertStringToInt( strIdDirectory ),
                getPlugin(  ), getUser(  ) );
        model.put( MARK_ENTRY_LIST, listEntry );
        model.put( MARK_ID_ENTRY_LIST, listIdEntry );
        model.put( MARK_ID_DIRECTORY, strIdDirectory );

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

        String[] listStrIdEntry = request.getParameterValues( PARAMETER_CONFIG_ENTRY );
        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        if ( listStrIdEntry != null )
        {
            for ( int i = 0; i < listStrIdEntry.length; i++ )
            {
                listIdEntry.add( Integer.valueOf( listStrIdEntry[i] ) );
            }
        }

        checkEntryGroup( listIdEntry );

        UrlItem url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );

        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

        if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_SAVE ) )
        {
            if ( StringUtils.isBlank( request.getParameter( PARAMETER_CREATECONFIG_NAME ) ) )
            {
                url = new UrlItem( JSP_CREATE_CONFIG_PRODUCER );
                url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

                if ( !listIdEntry.isEmpty(  ) )
                {
                    for ( Integer idEntry : listIdEntry )
                    {
                        url.addParameter( PARAMETER_CONFIG_ENTRY, String.valueOf( idEntry ) );
                    }
                }

                return AdminMessageService.getMessageUrl( request, MESSAGE_NAME_CONFIG_MISSED, url.getUrl(  ),
                    AdminMessage.TYPE_ERROR );
            }
            else
            {
                ConfigProducerService configProducerService = getDirectoryManageZipBasketService(  );

                configProducerService.addNewConfig( getPlugin(  ), request.getParameter( PARAMETER_CREATECONFIG_NAME ),
                    DirectoryUtils.convertStringToInt( strIdDirectory ), TYPE_CONFIG_PDF, listIdEntry );
            }

            return AdminMessageService.getMessageUrl( request, MESSAGE_ADD_NEW_CONFIG, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_CANCEL ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANCEL_CREATE, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else
        {
            String strUrlCreate = JSP_CREATE_CONFIG_PRODUCER_BIS + "?" + PARAMETER_ID_DIRECTORY + "=" + strIdDirectory;

            return doCheckAll( strUrlCreate, strIdDirectory, listIdEntry,
                request.getParameter( PARAMETER_CREATECONFIG_NAME ) );
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

        ConfigProducerService configProducerService = getDirectoryManageZipBasketService(  );
        configProducerService.deleteProducerConfig( getPlugin(  ),
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

        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        if ( listStrIdEntry != null )
        {
            for ( int i = 0; i < listStrIdEntry.length; i++ )
            {
                listIdEntry.add( Integer.valueOf( listStrIdEntry[i] ) );
            }
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        ConfigProducerService configProducerService = getDirectoryManageZipBasketService(  );

        configProducerService.loadListConfigEntry( getPlugin(  ),
            DirectoryUtils.convertStringToInt( strIdConfigProducer ) );

        List<IEntry> listEntry = DirectoryUtils.getFormEntries( DirectoryUtils.convertStringToInt( strIdDirectory ),
                getPlugin(  ), getUser(  ) );

        ConfigProducer configProducer = configProducerService.loadConfig( getPlugin(  ),
                DirectoryUtils.convertStringToInt( strIdConfigProducer ) );

        model.put( MARK_ENTRY_LIST, listEntry );

        if ( StringUtils.isNotBlank( strCheckPageConfig ) )
        {
            model.put( MARK_ID_ENTRY_LIST, listIdEntry );
        }
        else
        {
            model.put( MARK_ID_ENTRY_LIST,
                configProducerService.loadListConfigEntry( getPlugin(  ),
                    DirectoryUtils.convertStringToInt( strIdConfigProducer ) ) );
        }

        model.put( MARK_ID_DIRECTORY, strIdDirectory );
        model.put( MARK_ID_CONFIG_PRODUCER, strIdConfigProducer );
        model.put( MARK_CONFIG_PRODUCER, configProducer );

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
        List<Integer> listIdEntry = new ArrayList<Integer>(  );

        if ( listStrIdEntry != null )
        {
            for ( int i = 0; i < listStrIdEntry.length; i++ )
            {
                listIdEntry.add( Integer.valueOf( listStrIdEntry[i] ) );
            }
        }

        checkEntryGroup( listIdEntry );

        UrlItem url = new UrlItem( JSP_MANAGE_CONFIG_PRODUCER );

        url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );

        if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_SAVE ) )
        {
            if ( StringUtils.isBlank( request.getParameter( PARAMETER_CREATECONFIG_NAME ) ) )
            {
                url = new UrlItem( JSP_MODIFY_CONFIG_PRODUCER );
                url.addParameter( PARAMETER_ID_DIRECTORY, strIdDirectory );
                url.addParameter( PARAMETER_ID_CONFIG_PRODUCER, strIdConfigProducer );
                url.addParameter( PARAMETER_CHECK_PAGE_CONFIG, strCheckPageConfig );

                if ( !listIdEntry.isEmpty(  ) )
                {
                    for ( Integer idEntry : listIdEntry )
                    {
                        url.addParameter( PARAMETER_CONFIG_ENTRY, String.valueOf( idEntry ) );
                    }
                }

                return AdminMessageService.getMessageUrl( request, MESSAGE_NAME_CONFIG_MISSED, url.getUrl(  ),
                    AdminMessage.TYPE_ERROR );
            }
            else
            {
                ConfigProducerService configProducerService = getDirectoryManageZipBasketService(  );

                configProducerService.modifyProducerConfig( getPlugin(  ),
                    request.getParameter( PARAMETER_CREATECONFIG_NAME ),
                    DirectoryUtils.convertStringToInt( strIdConfigProducer ), TYPE_CONFIG_PDF, listIdEntry );
            }

            return AdminMessageService.getMessageUrl( request, MESSAGE_ADD_NEW_CONFIG, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else if ( request.getParameterMap(  ).containsKey( PARAMETER_CREATECONFIG_CANCEL ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANCEL_CREATE, url.getUrl(  ),
                AdminMessage.TYPE_INFO );
        }
        else
        {
            String strUrlModify = JSP_MODIFY_CONFIG_PRODUCER_BIS + "?" + PARAMETER_ID_DIRECTORY + "=" + strIdDirectory +
                "&" + PARAMETER_CHECK_PAGE_CONFIG + "=" + strIdConfigProducer + "&" + PARAMETER_ID_CONFIG_PRODUCER +
                "=" + strCheckPageConfig;

            return doCheckAll( strUrlModify, strIdDirectory, listIdEntry,
                request.getParameter( PARAMETER_CREATECONFIG_NAME ) );
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
        ConfigProducerService configProducerService = getDirectoryManageZipBasketService(  );
        configProducerService.copyProducerConfig( getPlugin(  ),
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
         * @param strUrlRedirect url to redirect
         * @param strIdDirectory id directory
         * @param listIdEntry list of id Entry
         * @param strNameconfig name of config
         * @return final url with params
         */
    private String doCheckAll( String strUrlRedirect, String strIdDirectory, List<Integer> listIdEntry,
        String strNameconfig )
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
                            strUrlRedirect += ( "&" + PARAMETER_CONFIG_ENTRY + "=" +
                            String.valueOf( child.getIdEntry(  ) ) );
                        }
                    }
                }
            }
            else
            {
                if ( !listIdEntry.contains( entry.getIdEntry(  ) ) )
                {
                    strUrlRedirect += ( "&" + PARAMETER_CONFIG_ENTRY + "=" + String.valueOf( entry.getIdEntry(  ) ) );
                }
            }
        }

        if ( StringUtils.isNotBlank( strNameconfig ) )
        {
            return strUrlRedirect += ( "&" + PARAMETER_CREATECONFIG_NAME + "=" + strNameconfig );
        }
        else
        {
            return strUrlRedirect;
        }
    }
}
