/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.pdfproducer.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryTypeDownloadUrl;
import fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.modules.pdfproducer.business.producerconfig.IConfigProducer;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;


/**
 * class for generate PDF document from directory records
 */
public final class PDFUtils
{
    // PROPERTIES
    private static final String PROPERTY_POLICE_FORMAT_DATE = "directory.pdfgenerate.format.date";
    private static final String PROPERTY_POLICE_SIZE_DATE = "directory.pdfgenerate.font.size.date";
    private static final String PROPERTY_POLICE_STYLE_DATE = "directory.pdfgenerate.font.style.date";
    private static final String PROPERTY_POLICE_ALIGN_DATE = "directory.pdfgenerate.font.align.date";
    private static final String PROPERTY_POLICE_SIZE_TITLE_DIRECTORY = "directory.pdfgenerate.font.size.title.directory";
    private static final String PROPERTY_POLICE_STYLE_TITLE_DIRECTORY = "directory.pdfgenerate.font.style.title.directory";
    private static final String PROPERTY_POLICE_SPACING_BEFORE_TITLE_DIRECTORY = "directory.pdfgenerate.font.spacing_before.title.directory";
    private static final String PROPERTY_POLICE_SPACING_AFTER_TITLE_DIRECTORY = "directory.pdfgenerate.font.spacing_after.title.directory";
    private static final String PROPERTY_POLICE_SIZE_ENTRY_GROUP = "directory.pdfgenerate.font.size.entry.group";
    private static final String PROPERTY_POLICE_STYLE_ENTRY_GROUP = "directory.pdfgenerate.font.style.entry.group";
    private static final String PROPERTY_POLICE_MARGIN_LEFT_ENTRY_GROUP = "directory.pdfgenerate.font.margin_left.entry.group";
    private static final String PROPERTY_POLICE_SPACING_BEFORE_ENTRY_GROUP = "directory.pdfgenerate.font.spacing_before.entry.group";
    private static final String PROPERTY_POLICE_SPACING_AFTER_ENTRY_GROUP = "directory.pdfgenerate.font.spacing_after.entry.group";
    private static final String PROPERTY_POLICE_SIZE_ENTRY_TITLE = "directory.pdfgenerate.font.size.entry.title";
    private static final String PROPERTY_POLICE_STYLE_ENTRY_TITLE = "directory.pdfgenerate.font.style.entry.title";
    private static final String PROPERTY_POLICE_SIZE_ENTRY_VALUE = "directory.pdfgenerate.font.size.entry.value";
    private static final String PROPERTY_POLICE_STYLE_ENTRY_VALUE = "directory.pdfgenerate.font.style.entry.value";
    private static final String PROPERTY_POLICE_MARGIN_LEFT_ENTRY_VALUE = "directory.pdfgenerate.font.margin_left.entry.value";
    private static final String PROPERTY_POLICE_NAME = "directory.pdfgenerate.font.name";
    private static final String PROPERTY_IMAGE_URL = "directory.pdfgenerate.image.url";
    private static final String PROPERTY_IMAGE_ALIGN = "directory.pdfgenerate.image.align";
    private static final String PROPERTY_IMAGE_FITWIDTH = "directory.pdfgenerate.image.fitWidth";
    private static final String PROPERTY_IMAGE_FITHEIGHT = "directory.pdfgenerate.image.fitHeight";

    // private static final String PROPERTY_MESSAGE_COULD_NOT_FETCH_FILE_NAME = "module.directory.pdfproducer.message.could_not_fetch_file_name";
    private static final String PARAMETER_ID_DIRECTORY_RECORD = "id_directory_record";
    private static final String PARAMETER_ID_RECORD = "id_record";

    // FIELDS
    private static final String FIELD_THUMBNAIL = "little_thumbnail";
    private static final String FIELD_BIG_THUMBNAIL = "big_thumbnail";

    // CONSTANTS
    private static final String DEFAULT_TYPE_FILE_NAME = "default";
    private static final String DIRECTORY_ENTRY_FILE_NAME = "directory_entry";

    /**
     * Constructor
     */
    private PDFUtils( )
    {
    }

    /**
     * method to create PDF without config
     * @param request request
     * @param strNameFile PDF name
     * @param out OutputStream
     * @param nIdRecord the id record
     * @param bExtractNotFilledField if true, extract empty fields, false
     *            otherwise
     */
    public static void doCreateDocumentPDF( HttpServletRequest request, String strNameFile, OutputStream out,
            int nIdRecord, Boolean bExtractNotFilledField )
    {
        List<Integer> listIdEntryConfig = new ArrayList<Integer>( );
        doCreateDocumentPDF( request, strNameFile, out, nIdRecord, listIdEntryConfig, bExtractNotFilledField );
    }

    /**
     * method to create PDF
     * @param request request
     * @param strNameFile PDF name
     * @param out OutputStream
     * @param nIdRecord the id record
     * @param listIdEntryConfig list of config id entry
     * @param bExtractNotFilledField if true, extract empty fields, false
     *            otherwise
     */
    public static void doCreateDocumentPDF( HttpServletRequest request, String strNameFile, OutputStream out,
            int nIdRecord, List<Integer> listIdEntryConfig, Boolean bExtractNotFilledField )
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        Locale locale = request.getLocale( );
        doCreateDocumentPDF( adminUser, locale, strNameFile, out, nIdRecord, listIdEntryConfig, bExtractNotFilledField );
    }

    /**
     * method to create PDF
     * @param adminUser The admin user
     * @param locale The locale
     * @param strNameFile PDF name
     * @param out OutputStream
     * @param nIdRecord the id record
     * @param listIdEntryConfig list of config id entry
     * @param bExtractNotFilledField if true, extract empty fields, false
     */
    public static void doCreateDocumentPDF( AdminUser adminUser, Locale locale, String strNameFile, OutputStream out,
            int nIdRecord, List<Integer> listIdEntryConfig, Boolean bExtractNotFilledField )
    {
        Document document = new Document( PageSize.A4 );

        Plugin plugin = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        EntryFilter filter;

        Record record = RecordHome.findByPrimaryKey( nIdRecord, plugin );

        filter = new EntryFilter( );
        filter.setIdDirectory( record.getDirectory( ).getIdDirectory( ) );
        filter.setIsGroup( EntryFilter.FILTER_TRUE );

        List<IEntry> listEntry = DirectoryUtils.getFormEntries( record.getDirectory( ).getIdDirectory( ), plugin,
                adminUser );
        int nIdDirectory = record.getDirectory( ).getIdDirectory( );
        Directory directory = DirectoryHome.findByPrimaryKey( nIdDirectory, plugin );

        try
        {
            PdfWriter.getInstance( document, out );
        }
        catch ( DocumentException e )
        {
            AppLogService.error( e );
        }

        document.open( );

        if ( record.getDateCreation( ) != null )
        {
            SimpleDateFormat monthDayYearformatter = new SimpleDateFormat(
                    AppPropertiesService.getProperty( PROPERTY_POLICE_FORMAT_DATE ) );

            Font fontDate = new Font( DirectoryUtils.convertStringToInt( AppPropertiesService
                    .getProperty( PROPERTY_POLICE_NAME ) ), DirectoryUtils.convertStringToInt( AppPropertiesService
                    .getProperty( PROPERTY_POLICE_SIZE_DATE ) ),
                    DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_STYLE_DATE ) ) );

            Paragraph paragraphDate = new Paragraph( new Phrase( monthDayYearformatter
                    .format( record.getDateCreation( ) ).toString( ), fontDate ) );

            paragraphDate.setAlignment( DirectoryUtils.convertStringToInt( AppPropertiesService
                    .getProperty( PROPERTY_POLICE_ALIGN_DATE ) ) );

            try
            {
                document.add( paragraphDate );
            }
            catch ( DocumentException e )
            {
                AppLogService.error( e );
            }
        }

        Image image;

        try
        {

            image = Image.getInstance( ImageIO.read( new File( AppPathService
                    .getAbsolutePathFromRelativePath( AppPropertiesService.getProperty( PROPERTY_IMAGE_URL ) ) ) ),
                    null );
            image.setAlignment( DirectoryUtils.convertStringToInt( AppPropertiesService
                    .getProperty( PROPERTY_IMAGE_ALIGN ) ) );
            float fitWidth;
            float fitHeight;

            try
            {
                fitWidth = Float.parseFloat( AppPropertiesService.getProperty( PROPERTY_IMAGE_FITWIDTH ) );
                fitHeight = Float.parseFloat( AppPropertiesService.getProperty( PROPERTY_IMAGE_FITHEIGHT ) );
            }
            catch ( NumberFormatException e )
            {
                fitWidth = 100f;
                fitHeight = 100f;
            }

            image.scaleToFit( fitWidth, fitHeight );

            try
            {
                document.add( image );
            }
            catch ( DocumentException e )
            {
                AppLogService.error( e );
            }
        }
        catch ( BadElementException e )
        {
            AppLogService.error( e );
        }
        catch ( MalformedURLException e )
        {
            AppLogService.error( e );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }

        directory.getTitle( );

        Font fontTitle = new Font( DirectoryUtils.convertStringToInt( AppPropertiesService
                .getProperty( PROPERTY_POLICE_NAME ) ), DirectoryUtils.convertStringToInt( AppPropertiesService
                .getProperty( PROPERTY_POLICE_SIZE_TITLE_DIRECTORY ) ),
                DirectoryUtils.convertStringToInt( AppPropertiesService
                        .getProperty( PROPERTY_POLICE_STYLE_TITLE_DIRECTORY ) ) );
        fontTitle.isUnderlined( );

        Paragraph paragraphHeader = new Paragraph( new Phrase( directory.getTitle( ), fontTitle ) );
        paragraphHeader.setAlignment( Element.ALIGN_CENTER );
        paragraphHeader.setSpacingBefore( DirectoryUtils.convertStringToInt( AppPropertiesService
                .getProperty( PROPERTY_POLICE_SPACING_BEFORE_TITLE_DIRECTORY ) ) );
        paragraphHeader.setSpacingAfter( DirectoryUtils.convertStringToInt( AppPropertiesService
                .getProperty( PROPERTY_POLICE_SPACING_AFTER_TITLE_DIRECTORY ) ) );

        try
        {
            document.add( paragraphHeader );
        }
        catch ( DocumentException e )
        {
            AppLogService.error( e );
        }

        builderPDFWithEntry( document, plugin, nIdRecord, listEntry, listIdEntryConfig, locale, bExtractNotFilledField );
        document.close( );
    }

    /**
     * method to builder PDF with directory entry
     * @param document document pdf
     * @param plugin plugin
     * @param nIdRecord id record
     * @param listEntry list of entry
     * @param listIdEntryConfig list of config id entry
     * @param locale the locale
     * @param bExtractNotFilledField if true, extract empty fields, false
     */
    private static void builderPDFWithEntry( Document document, Plugin plugin, int nIdRecord, List<IEntry> listEntry,
            List<Integer> listIdEntryConfig, Locale locale, Boolean bExtractNotFilledField )
    {
        Map<String, List<RecordField>> mapIdEntryListRecordField = DirectoryUtils.getMapIdEntryListRecordField(
                listEntry, nIdRecord, plugin );

        for ( IEntry entry : listEntry )
        {
            if ( entry.getEntryType( ).getGroup( )
                    && ( listIdEntryConfig.isEmpty( ) || listIdEntryConfig.contains( Integer.valueOf( entry
                            .getIdEntry( ) ) ) ) )
            {
                Font fontEntryTitleGroup = new Font( DirectoryUtils.convertStringToInt( AppPropertiesService
                        .getProperty( PROPERTY_POLICE_NAME ) ), DirectoryUtils.convertStringToInt( AppPropertiesService
                        .getProperty( PROPERTY_POLICE_SIZE_ENTRY_GROUP ) ),
                        DirectoryUtils.convertStringToInt( AppPropertiesService
                                .getProperty( PROPERTY_POLICE_STYLE_ENTRY_GROUP ) ) );
                Paragraph paragraphTitleGroup = new Paragraph( new Phrase( entry.getTitle( ), fontEntryTitleGroup ) );
                paragraphTitleGroup.setAlignment( Element.ALIGN_LEFT );
                paragraphTitleGroup.setIndentationLeft( DirectoryUtils.convertStringToInt( AppPropertiesService
                        .getProperty( PROPERTY_POLICE_MARGIN_LEFT_ENTRY_GROUP ) ) );
                paragraphTitleGroup.setSpacingBefore( DirectoryUtils.convertStringToInt( AppPropertiesService
                        .getProperty( PROPERTY_POLICE_SPACING_BEFORE_ENTRY_GROUP ) ) );
                paragraphTitleGroup.setSpacingAfter( DirectoryUtils.convertStringToInt( AppPropertiesService
                        .getProperty( PROPERTY_POLICE_SPACING_AFTER_ENTRY_GROUP ) ) );

                try
                {
                    document.add( paragraphTitleGroup );
                }
                catch ( DocumentException e )
                {
                    AppLogService.error( e );
                }

                if ( entry.getChildren( ) != null )
                {
                    for ( IEntry child : entry.getChildren( ) )
                    {
                        if ( listIdEntryConfig.isEmpty( )
                                || listIdEntryConfig.contains( Integer.valueOf( child.getIdEntry( ) ) ) )
                        {
                            try
                            {
                                builFieldsInPDF(
                                        mapIdEntryListRecordField.get( Integer.toString( child.getIdEntry( ) ) ),
                                        document, child, locale, bExtractNotFilledField );
                            }
                            catch ( DocumentException e )
                            {
                                AppLogService.error( e );
                            }
                        }
                    }
                }
            }
            else
            {
                if ( listIdEntryConfig.isEmpty( )
                        || listIdEntryConfig.contains( Integer.valueOf( entry.getIdEntry( ) ) ) )
                {
                    try
                    {
                        builFieldsInPDF( mapIdEntryListRecordField.get( Integer.toString( entry.getIdEntry( ) ) ),
                                document, entry, locale, bExtractNotFilledField );
                    }
                    catch ( DocumentException e )
                    {
                        AppLogService.error( e );
                    }
                }
            }
        }
    }

    /**
     * This method build the different label and value of directory fields in
     * the document PDF
     * 
     * @param listRecordFields recordfield map
     * @param document document pdf
     * @param entry entry
     * @param locale the locale
     * @param bExtractNotFilledField if true, extract empty fields, false
     * @throws DocumentException DocumentException
     */
    private static void builFieldsInPDF( List<RecordField> listRecordFields, Document document, IEntry entry,
            Locale locale, Boolean bExtractNotFilledField ) throws DocumentException
    {
        Phrase phraseEntry = new Phrase( );
        Paragraph paragraphEntry = new Paragraph( );

        if ( StringUtils.isNotBlank( entry.getTitle( ) ) )
        {
            entry.getTitle( );

            Font fontEntryTitle = new Font( DirectoryUtils.convertStringToInt( AppPropertiesService
                    .getProperty( PROPERTY_POLICE_NAME ) ), DirectoryUtils.convertStringToInt( AppPropertiesService
                    .getProperty( PROPERTY_POLICE_SIZE_ENTRY_TITLE ) ),
                    DirectoryUtils.convertStringToInt( AppPropertiesService
                            .getProperty( PROPERTY_POLICE_STYLE_ENTRY_TITLE ) ) );
            Chunk chunkEntryTitle = new Chunk( entry.getTitle( ), fontEntryTitle );
            phraseEntry.add( chunkEntryTitle );
            phraseEntry.add( " : " );
        }

        if ( ( listRecordFields != null ) && ( listRecordFields.size( ) > 0 ) )
        {
            // Must separate the behaviour whether the list of record fiels contains 1 or more record fields
            if ( ( listRecordFields.size( ) > 1 )
                    && !( entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation ) )
            {
                builFieldsInParagraph( listRecordFields, entry, locale, phraseEntry, paragraphEntry,
                        bExtractNotFilledField );
            }
            else
            {
                builFieldsInSinglePhrase( listRecordFields, entry, locale, phraseEntry, paragraphEntry,
                        bExtractNotFilledField );
            }
        }

        document.add( paragraphEntry );
    }

    /**
     * Build the fields in a whole paragraph.
     * @param listRecordFields the list of record fields
     * @param entry the entry
     * @param locale the locale
     * @param phraseEntry the phrase entry
     * @param paragraphEntry the paragraph entry
     * @param bExtractNotFilledField if true, extract empty fields, false
     * @throws DocumentException exception if there is an error
     */
    private static void builFieldsInParagraph( List<RecordField> listRecordFields, IEntry entry, Locale locale,
            Phrase phraseEntry, Paragraph paragraphEntry, Boolean bExtractNotFilledField ) throws DocumentException
    {
        com.lowagie.text.List listValue = new com.lowagie.text.List( true );
        listValue.setPreSymbol( "- " );
        listValue.setNumbered( false );
        listValue.setIndentationLeft( DirectoryUtils.convertStringToInt( AppPropertiesService
                .getProperty( PROPERTY_POLICE_MARGIN_LEFT_ENTRY_VALUE ) ) );

        Font fontEntryValue = new Font(
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_NAME ) ),
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_SIZE_ENTRY_VALUE ) ),
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_STYLE_ENTRY_VALUE ) ) );

        for ( RecordField recordField : listRecordFields )
        {
            String strValue = StringUtils.EMPTY;

            if ( entry instanceof EntryTypeDownloadUrl && StringUtils.isNotBlank( recordField.getFileName( ) ) )
            {
                strValue = recordField.getFileName( );
            }
            else if ( ( recordField.getFile( ) != null ) && StringUtils.isNotBlank( recordField.getFile( ).getTitle( ) ) )
            {
                // The thumbnails and big thumbnails should not be displayed
                if ( !( ( StringUtils.isNotBlank( recordField.getValue( ) ) && recordField.getValue( ).startsWith(
                        FIELD_THUMBNAIL ) ) || ( StringUtils.isNotBlank( recordField.getValue( ) ) && recordField
                        .getValue( ).startsWith( FIELD_BIG_THUMBNAIL ) ) ) )
                {
                    strValue = recordField.getFile( ).getTitle( );
                }
            }
            else
            {
                strValue = entry.convertRecordFieldValueToString( recordField, locale, false, false );
            }

            if ( bExtractNotFilledField || ( !bExtractNotFilledField && StringUtils.isNotBlank( strValue ) ) )
            {
                listValue.add( new ListItem( strValue, fontEntryValue ) );
            }
        }

        if ( bExtractNotFilledField
                || ( !bExtractNotFilledField && CollectionUtils.isNotEmpty( listValue.getItems( ) ) ) )
        {
            paragraphEntry.add( phraseEntry );
            paragraphEntry.add( listValue );
        }
    }

    /**
     * Build the fields in a single paragraph.
     * @param listRecordFields the list of record fields
     * @param entry the entry
     * @param locale the locale
     * @param phraseEntry the phrase entry
     * @param paragraphEntry the paragraph entry
     * @param bExtractNotFilledField if true, extract empty fields, false
     * @throws DocumentException exception if there is an error
     */
    private static void builFieldsInSinglePhrase( List<RecordField> listRecordFields, IEntry entry, Locale locale,
            Phrase phraseEntry, Paragraph paragraphEntry, Boolean bExtractNotFilledField ) throws DocumentException
    {
        RecordField recordField = listRecordFields.get( 0 );
        Chunk chunkEntryValue = null;
        Font fontEntryValue = new Font(
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_NAME ) ),
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_SIZE_ENTRY_VALUE ) ),
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_POLICE_STYLE_ENTRY_VALUE ) ) );

        if ( entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeDownloadUrl )
        {
            if ( StringUtils.isNotBlank( recordField.getFileName( ) ) )
            {
                chunkEntryValue = new Chunk( recordField.getFileName( ) );
            }
            else
            {
                chunkEntryValue = new Chunk( StringUtils.EMPTY );
            }
        }
        else if ( entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation )
        {
            for ( RecordField recordFieldGeo : listRecordFields )
            {
                if ( ( recordFieldGeo.getField( ) != null )
                        && EntryTypeGeolocation.CONSTANT_ADDRESS.equals( recordFieldGeo.getField( ).getTitle( ) ) )
                {
                    chunkEntryValue = new Chunk( entry.convertRecordFieldValueToString( recordFieldGeo, locale, false,
                            false ), fontEntryValue );
                }
            }
        }
        else if ( entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeCheckBox
                || entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeSelect
                || entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeRadioButton )
        {
            chunkEntryValue = new Chunk( entry.convertRecordFieldTitleToString( recordField, locale, false ),
                    fontEntryValue );
        }
        else if ( entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeFile
                || entry instanceof fr.paris.lutece.plugins.directory.business.EntryTypeImg )
        {
            String strFileName = StringUtils.EMPTY;

            if ( ( recordField.getFile( ) != null ) && StringUtils.isNotBlank( recordField.getFile( ).getTitle( ) ) )
            {
                // The thumbnails and big thumbnails should not be displayed
                if ( !( ( StringUtils.isNotBlank( recordField.getValue( ) ) && recordField.getValue( ).startsWith(
                        FIELD_THUMBNAIL ) ) || ( StringUtils.isNotBlank( recordField.getValue( ) ) && recordField
                        .getValue( ).startsWith( FIELD_BIG_THUMBNAIL ) ) ) )
                {
                    strFileName = recordField.getFile( ).getTitle( );
                }
            }

            chunkEntryValue = new Chunk( strFileName, fontEntryValue );
        }
        else
        {
            chunkEntryValue = new Chunk( entry.convertRecordFieldValueToString( recordField, locale, false, false ),
                    fontEntryValue );
        }

        if ( chunkEntryValue != null )
        {
            if ( bExtractNotFilledField
                    || ( !bExtractNotFilledField && StringUtils.isNotBlank( chunkEntryValue.getContent( ) ) ) )
            {
                phraseEntry.add( chunkEntryValue );
                paragraphEntry.add( phraseEntry );
            }
        }
    }

    /**
     * Method to download a PDF file generated by directory record
     * @param request request
     * @param response response
     * @param plugin plugin
     * @param configProducer configuration
     * @param listIdEntryConfig list of config id entry
     * @param locale locale
     * @param nIdRecord the id record
     */
    public static void doDownloadPDF( HttpServletRequest request, HttpServletResponse response, Plugin plugin,
            IConfigProducer configProducer, List<Integer> listIdEntryConfig, Locale locale, int nIdRecord )
    {
        Record record = RecordHome.findByPrimaryKey( nIdRecord, plugin );
        Directory directory = DirectoryHome.findByPrimaryKey( record.getDirectory( ).getIdDirectory( ), plugin );

        String strName = getFileNameFromConfig( directory, configProducer, nIdRecord, locale );
        String strFileName = doPurgeNameFile( strName ) + ".pdf";

        response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
        response.setHeader( "Pragma", "public" );
        response.setHeader( "Expires", "0" );
        response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );

        response.setContentType( "application/pdf" );

        OutputStream os = null;

        try
        {
            os = response.getOutputStream( );

            if ( listIdEntryConfig != null )
            {
                PDFUtils.doCreateDocumentPDF( request, strName, os, nIdRecord, listIdEntryConfig,
                        configProducer.getExtractNotFilled( ) );
            }
            else
            {
                PDFUtils.doCreateDocumentPDF( request, strName, os, nIdRecord, configProducer.getExtractNotFilled( ) );
            }
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
        finally
        {
            IOUtils.closeQuietly( os );
        }
    }

    public static String getFileNameFromConfig( Directory directory, IConfigProducer configProducer, int nIdRecord,
            Locale locale )
    {
        String strName = null;
        String strTypeConfigFileName = configProducer.getTypeConfigFileName( );
        if ( strTypeConfigFileName.equals( DEFAULT_TYPE_FILE_NAME ) )
        {
            strName = StringUtil.replaceAccent( directory.getTitle( ) ).replace( " ", "_" ) + "_" + nIdRecord;
        }
        else if ( strTypeConfigFileName.equals( DIRECTORY_ENTRY_FILE_NAME ) )
        {
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
            RecordFieldFilter filter = new RecordFieldFilter( );
            filter.setIdRecord( nIdRecord );
            filter.setIdEntry( configProducer.getIdEntryFileName( ) );

            List<RecordField> listRecordField = RecordFieldHome.getRecordFieldList( filter, pluginDirectory );

            for ( RecordField recordField : listRecordField )
            {
                strName = recordField.getEntry( ).convertRecordFieldValueToString( recordField, locale, false, false );
            }
        }
        else
        {
            strName = configProducer.getTextFileName( ) + "_" + nIdRecord;
        }
        return strName;
    }

    /**
     * Method to purge file name, (replace accent and punctuation)
     * @param strNameFile name file
     * @return purged name file
     */
    public static String doPurgeNameFile( String strNameFile )
    {
        return StringUtil.replaceAccent( strNameFile ).replaceAll( "\\W", "_" );
    }
}
