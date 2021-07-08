package com.example.easyruledemo.entity.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 11:48
 * transform from com.example.easyruledemo.entity.EwsRuleEntity.actions
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel("ews执行动作实体")
public class EwsActionsEntity {
    /**
     * The AssignCategories action.
     */
    @ApiModelProperty("分类包含什么")
    private List<String> assignCategories;

    /**
     * FolderId
     * The CopyToFolder action.
     */
    @ApiModelProperty("拷贝到文件夹folderId unionId")
    private String copyToFolder;

    /**
     * The Delete action.
     */
    @ApiModelProperty("进行删除")
    private boolean delete;

    /**
     * The ForwardAsAttachmentToRecipients action.
     */
    @ApiModelProperty("作为附件转发给收件人邮箱s email address")
    private List<String> forwardAsAttachmentToRecipients;

    /**
     * The ForwardToRecipients action.
     */
    @ApiModelProperty("转发给收件人邮箱s email address")
    private List<String> forwardToRecipients;

    /**
     * The MarkAsRead action.
     */
    @ApiModelProperty("标记已读")
    private boolean markAsRead;

    /**
     * FolderId
     * The MoveToFolder action.
     */
    @ApiModelProperty("移动到文件夹folderId unionId")
    private String moveToFolder;

    /**
     * The PermanentDelete action.
     */
    @ApiModelProperty("永久删除")
    private boolean permanentDelete;

    /**
     * The RedirectToRecipients action.
     */
    @ApiModelProperty("重定向到收件人邮箱s email address")
    private List<String> redirectToRecipients;

//    /**
//     * ItemId
//     * The ServerReplyWithMessage action.
//     */
//    private String serverReplyWithMessage;

    /**
     * The StopProcessingRules action.
     */
    @ApiModelProperty("停止执行规则")
    private boolean stopProcessingRules;



}
