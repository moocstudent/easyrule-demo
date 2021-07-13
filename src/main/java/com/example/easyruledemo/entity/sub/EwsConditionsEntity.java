package com.example.easyruledemo.entity.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @Author: zhangQi
 * @Date: 2021-07-03 11:42
 * transform from com.example.easyruledemo.entity.EwsRuleEntity.conditions
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel("ews执行条件实体")
public class EwsConditionsEntity {

    /**
     * The HasCategories predicate.
     */
    @ApiModelProperty("分类包含什么")
    private List<String> categories;

    /**
     * The ContainsSubjectStrings predicate.
     */
    @ApiModelProperty("标头包含什么字符串")
    private List<String> containsSubjectStrings;

    /**
     * The ContainsBodyStrings predicate.
     */
    @ApiModelProperty("正文包含什么字符串")
    private List<String> containsBodyStrings;
    /**
     * The ContainsHeaderStrings predicate.
     */
    @ApiModelProperty("头部包含什么字符串")
    private List<String> containsHeaderStrings;

    /**
     * The ContainsRecipientStrings predicate.
     */
    @ApiModelProperty("包含收件人字符串")
    private List<String> containsRecipientStrings;

    /**
     * The ContainsSenderStrings predicate.
     */
    @ApiModelProperty("包含发件人字符串")
    private List<String> containsSenderStrings;

    /**
     * The ContainsSubjectOrBodyStrings predicate.
     */
    @ApiModelProperty("包含标题/正文字符串")
    private List<String> containsSubjectOrBodyStrings;

    /**
     * The FromAddresses predicate.
     */
    @ApiModelProperty("发件人email address满足")
    private List<String> fromAddresses;

    /**
     * The FromConnectedAccounts predicate.
     */
    @ApiModelProperty("关联账户")
    private List<String> fromConnectedAccounts;

    /**
     * The HasAttachments predicate.
     */
    @ApiModelProperty("是否有附件")
    private boolean hasAttachments;

}
