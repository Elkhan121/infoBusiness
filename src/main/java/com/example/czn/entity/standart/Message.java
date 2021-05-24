package com.example.czn.entity.standart;

import com.example.czn.util.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter @Setter
@NoArgsConstructor
@ToString
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String   name;
    private String   photo;
    private Long     keyboardId;
    private String   file;
    private FileType typeFile;
//    private Language language;
    private int langId;

    public void setFile(String file, FileType fileType) {
        this.file = file;
        this.typeFile = fileType;
    }
}
