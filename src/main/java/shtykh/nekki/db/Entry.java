/***************       BEGIN-STANDARD-COPYRIGHT      ***************

 Copyright (c) 2009-2015, Spirent Communications.

 All rights reserved. Proprietary and confidential information of Spirent Communications.

 ***************        END-STANDARD-COPYRIGHT       ***************/

package shtykh.nekki.db;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "entry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Entry {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "pg-uuid")
	private UUID id;

	@NotNull
	@Size(max = 1024)
	@Column(name = "content")
	private String content;

	@NotNull
	@Column(name = "creationDate", nullable = false)
	@Type(type = "org.hibernate.type.DateType")
	private Date creationDate;
	
	private transient File file;

	public Entry(String content, Date creationDate) {
		this.content = content;
		this.creationDate = creationDate;
	}
	
	public Entry(){

	}

	@Override
	public String toString() {
		return "Entry{content=" + content + ", creationDate=" + creationDate + "}";
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
