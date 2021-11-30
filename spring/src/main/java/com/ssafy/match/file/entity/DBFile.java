package com.ssafy.match.file.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity(name = "matching.files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DBFile {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String file_name;
    private String file_type;
    private String download_uri;

    @Lob
    private byte[] data;

    public DBFile(String file_name, String file_type, byte[] data) {
        this.file_name = file_name;
        this.file_type = file_type;
        this.data = data;
    }
}
