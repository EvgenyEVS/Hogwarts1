package org.skypro.homeworks.hogwarts1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AvatarDto {
    private Long Id;
    private String filePath;
    private Long fileSize;
    private String mediaType;
    private Long studentId;
    private String studentName;

    public AvatarDto(Long id, String studentName, Long studentId, String mediaType, Long fileSize, String filePath) {
        Id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }
}
