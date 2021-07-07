package com.example.easyruledemo.delegate;

import com.example.easyruledemo.container.EwsExContainer;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.response.GetItemResponse;
import microsoft.exchange.webservices.data.core.response.ServiceResponseCollection;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.NotificationEvent;
import microsoft.exchange.webservices.data.notification.NotificationEventArgs;
import microsoft.exchange.webservices.data.notification.StreamingSubscriptionConnection;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 14:23
 * 之前用于邮件事件提醒,这里暂时没有使用这里回调提醒方式,不过是可执行的
 */
@Component
@Deprecated
public class EmailNotifyDelegate implements StreamingSubscriptionConnection.INotificationEventDelegate {
    //提醒委派
    @Override
    public void notificationEventDelegate(Object sender, NotificationEventArgs args) {
        //https://github.com/OfficeDev/ews-java-api/wiki/Getting-Started-Guide#streamingnotification
        List<ItemId> newEmailIds = null;
        Iterator<NotificationEvent> notifyEventIt = args.getEvents().iterator();
        int itemCount = 0;
        while (notifyEventIt.hasNext()) {
            ItemEvent itemEvent = (ItemEvent)notifyEventIt.next();
            if (itemEvent != null)
            {
                if(newEmailIds==null){
                    newEmailIds = new ArrayList<>();
                }
                newEmailIds.add(itemEvent.getItemId());
                itemCount++;
            }
        }
        if(itemCount>0){
            // Now retrieve the Subject property of all the new emails in one call to EWS.
            try {
                ServiceResponseCollection<GetItemResponse> responses = EwsExContainer.defaultExchangeService().bindToItems(newEmailIds, new PropertySet(ItemSchema.Subject));
                System.out.println("count=======" + responses.getCount());

                //this.listBox1.Items.Add(string.Format("{0} new mail(s)", newMailsIds.Count));

                for(GetItemResponse response : responses)
                {
                    System.out.println("count=======" + responses.getClass().getName());
                    System.out.println("subject=======" + response.getItem().getSubject());
                    System.out.println("i don't need:"+response.getItem().getId());
                    // Console.WriteLine("subject====" + response.Item.Subject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
