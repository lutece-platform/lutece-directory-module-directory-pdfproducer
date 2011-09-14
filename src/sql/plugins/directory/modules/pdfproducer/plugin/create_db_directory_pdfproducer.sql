DROP TABLE IF EXISTS directory_config_producer;
DROP TABLE IF EXISTS directory_config_entry;
DROP TABLE IF EXISTS directory_default_config;

/*==============================================================*/
/* Table structure for table directory_config_pdf				*/
/*==============================================================*/
CREATE TABLE directory_config_producer (
  id_config INT DEFAULT 0 NOT NULL,
  name VARCHAR(255) DEFAULT NULL,
  id_entry_name_file INT DEFAULT NULL,
  id_directory INT DEFAULT NULL,
  config_type VARCHAR(255) DEFAULT NULL,
  text_file_name VARCHAR(255) DEFAULT NULL,
  type_config_file_name VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (id_config)
  );
  
/*==============================================================*/
/* Table structure for table directory_config_entry				*/
/*==============================================================*/
CREATE TABLE directory_config_entry (
  id_config INT DEFAULT NULL,
  id_entry INT DEFAULT NULL,
  PRIMARY KEY  (id_config,id_entry)
  );
  
/*==============================================================*/
/* Table structure for table directory_default_config			*/
/*==============================================================*/
CREATE TABLE directory_default_config (
  id_config INT DEFAULT NULL,
  id_directory INT DEFAULT NULL,
  PRIMARY KEY  (id_config,id_directory)
  );
