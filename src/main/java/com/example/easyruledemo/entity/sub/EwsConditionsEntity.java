package com.example.easyruledemo.entity.sub;

import lombok.*;
import lombok.experimental.Accessors;
import microsoft.exchange.webservices.data.property.complex.StringList;

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
public class EwsConditionsEntity {

    /**
     * The HasCategories predicate.
     */
    private List<String> categories;

    /**
     * The ContainsSubjectStrings predicate.
     */
    private List<String> containsSubjectStrings;

    /**
     * The ContainsBodyStrings predicate.
     */
    private List<String> containsBodyStrings;
    /**
     * The ContainsHeaderStrings predicate.
     */
    private List<String> containsHeaderStrings;

    /**
     * The ContainsRecipientStrings predicate.
     */
    private List<String> containsRecipientStrings;

    /**
     * The ContainsSenderStrings predicate.
     */
    private List<String> containsSenderStrings;

    /**
     * The ContainsSubjectOrBodyStrings predicate.
     */
    private List<String> containsSubjectOrBodyStrings;

    /**
     * The FromAddresses predicate.
     */
    private List<String> fromAddresses;

    /**
     * The FromConnectedAccounts predicate.
     */
    private List<String> fromConnectedAccounts;

    /**
     * The HasAttachments predicate.
     */
    private boolean hasAttachments;

}
