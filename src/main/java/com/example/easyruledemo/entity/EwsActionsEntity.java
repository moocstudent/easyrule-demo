package com.example.easyruledemo.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 11:48
 * transform from com.example.easyruledemo.entity.rule.EwsRuleEntity.actions
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class EwsActionsEntity {
    /**
     * The AssignCategories action.
     */
    private List<String> assignCategories;

    /**
     * FolderId
     * The CopyToFolder action.
     */
    private String copyToFolder;

    /**
     * The Delete action.
     */
    private boolean delete;

    /**
     * The ForwardAsAttachmentToRecipients action.
     */
    private List<String> forwardAsAttachmentToRecipients;

    /**
     * The ForwardToRecipients action.
     */
    private List<String> forwardToRecipients;

    /**
     * The MarkAsRead action.
     */
    private boolean markAsRead;

    /**
     * FolderId
     * The MoveToFolder action.
     */
    private String moveToFolder;

    /**
     * The PermanentDelete action.
     */
    private boolean permanentDelete;

    /**
     * The RedirectToRecipients action.
     */
    private List<String> redirectToRecipients;

//    /**
//     * ItemId
//     * The ServerReplyWithMessage action.
//     */
//    private String serverReplyWithMessage;

    /**
     * The StopProcessingRules action.
     */
    private boolean stopProcessingRules;



}
