package com.roadmateserver.root.dto.cache;

import com.roadmateserver.root.dto.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationListCache implements Serializable {

    private List<NotificationDTO> notificationList;
}
