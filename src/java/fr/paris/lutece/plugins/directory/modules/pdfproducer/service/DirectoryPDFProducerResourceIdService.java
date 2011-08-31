package fr.paris.lutece.plugins.directory.modules.pdfproducer.service;

import java.util.Locale;

import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

/**
 * DirectoryPDFProducerResourceIdService
 *
 */
public class DirectoryPDFProducerResourceIdService extends ResourceIdService
{

	/** Permission for generate zip */
    public static final String PERMISSION_GENERATE_PDF = "PDFPRODUCER";
    
    /** Permission for mylutece user visualisation */
    private static final String RESOURCE_TYPE = "DIRECTORY_PDFPRODUCER_TYPE";
    private static final String PROPERTY_LABEL_GENERATE_PDF = "module.directory.pdfproducer.permission.label.generate_pdf";
	
	@Override
	public ReferenceList getResourceIdList(Locale locale) {
		return DirectoryHome.getDirectoryList( PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ) );
	}

	@Override
	public String getTitle(String strId, Locale locale) 
	{
		return null;
	}

	@Override
	public void register() 
	{
		ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( DirectoryPDFProducerResourceIdService.class.getName(  ) );
        rt.setPluginName( DirectoryPDFProducerPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_GENERATE_PDF );
        
		Permission p = new Permission(  );
		p.setPermissionKey( PERMISSION_GENERATE_PDF );
        p.setPermissionTitleKey( PROPERTY_LABEL_GENERATE_PDF );
        rt.registerPermission( p );	
        
        ResourceTypeManager.registerResourceType( rt );
	}

}
