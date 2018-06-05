CREATE TABLE IF NOT EXISTS auth (
  id              int(10) NOT NULL AUTO_INCREMENT,
  user_id         int(10) NOT NULL,
  provider        varchar(200) NOT NULL,
  provider_key    varchar(200) NOT NULL,
  access_token    varchar(200) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `uniq_key` (`user_id`, `provider`, `provider_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;