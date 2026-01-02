package org.skypro.homeworks.hogwarts1.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
//@ToString(exclude = "student")

public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private Long fileSize;
    private String mediaType;

    @Lob
    private byte[] data;

    public Avatar(Long id, String filePath,
                  Long fileSize, String mediaType,
                  byte[] data, Student student) {

        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    @OneToOne
    Student student;
}
