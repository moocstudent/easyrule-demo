人总要为自己加油喝彩


1.基础表设计:
sql/schema.sql为基础数据库表设计,  
其中包含4个主体:分别为  
ews_mail_config>即为邮箱配置主体    
ews_mail_topic>即为邮箱规则主题主体,其中一个配置主题可包含一或多条规则 
ews_rule>即为邮件配置规则主表,其中根据level来断定这个rule会不会和其它的有冲突    
ews_mail_folder>即为邮箱配置主题(规则)后,规则所需要用到的建立的文件夹    
两个关联表:  
ews_topic_rule_relation>邮件配置主题与规则关联表,其中关联关系为,每个主题下配置的规则即使多条也不能重复rule_level  
ews_rule_folder_relation>规则与所需创建文件夹的关联表,其中一条规则关联到的folder不能重复    

2.执行流程:     
(1)
通过配置好相关参数后, 
如单独页面配置邮箱,为邮箱指定收件主题(下拉选择)   
而收件主题需要先进行预设,   
预设时是先填入收件主题相关参数,如收件主题名称,介绍,再选定相应的不同level的规则1,2,3    
不同level的规则1,2,3会将对应的ruleId记录到主题config做为这条主题唯一unique的配置串(用以标识这是已经存在的主题)     
而收件规则是需要先进行预设,
预设规则时填入规则名,规则对应相关conditions,actions,item_action_type,item_actions,rule_level等主要字段,  
并配置关联好相应该规则对应用到的ews_mail_folder,    
而ews_mail_folder需要配置好对应的相关参数,并在邮箱配置了主题后,根据主题内的一条或多条规则并规则对应到的    
一个或多个文件夹folder,将其中的folder_name生成到ews远端邮箱生成该文件夹.将生成的文件夹的返回的folderId(unionId),    
将其重新赋值写入数据库ews_mail_folder的folder_id字段进行存储,  
(2) 
整体正常设定预设值之后,通过配置邮箱的规则主题topic,保存topic后根据rule对应到的要生成的文件夹生成新的文件夹到指定邮箱, 
会根据该主题的topic来执行不同的ruleJob(task),比如邮件筛选标题含***字眼以及带有附件,那么移入生成后的文件夹内,  
再之后每日的task(定时每日一次的)初始化邮箱的新邮件订阅(可针对多个收件邮箱生成多个,有效期最长24小时,即可每日定时执行一次), 
执行完成每日的订阅后,需要等待一段时间(大概几分钟,代码里可以先不进行处理),定时从订阅中获取新邮件事件,此时已经通过订阅邮件的    
指定文件夹进行了相关订阅,如"待下载附件邮件"文件夹  
(3)
事件处理之后,将邮件中附件进行下载并根据ews_rule表中的item_action_type以及item_actions判定将附件下载到指定目录.  
附件下载成功后,将此邮件移入"已下载附件邮件"文件夹中,流程结束

3.相关进度:     
(1)
前期主要完成整体根据containsSubjectString(标题包含什么字眼),以及是否包含附件(getHasAttachments())来判定了这封邮件
应该移入指定的邮件文件夹,       
(2)
对该文件夹进行新邮件监听,有新邮件到了,则说明是满足条件的邮件,则进行task轮询获取其中的邮件实体,附件,进行下载,再将邮件移入
已下载附件文件夹.       
(3)
单元测试中,对ews的Rule以及对数据库返回的Rule相关字段conditions,actions进行了转换,而再将规则设置到邮箱内时,应该再根据
priority权重来设定被设定的多条规则的优先级,这里其实priority与rule_level是类似的,priority与rule_level类似,权重在rule表中是一整个规则的权重设置,
比较难判定这个规则的先后执行顺序,而参照ews_topic_rule_relation的更改,其实也应该在设定邮件topic时,实时去指定选中规则的执行先后顺序,而先后,
必然根据priority,但是否是可以放到一起做为同一主题的规则们,则需要根据rule_level(或rule_type)进行区分,即是说,priority是指定的,
而顺序的操作也要参照可行性,而rule_level(或rule_type)是预设置的.rule_level指定组合,priority指定顺序          
(4)
需要对相关数据库连接以及增删改查,联查,如获取该规则的权重priority,就从ews_topic_rule_relation进行联查priority字段后映射回rule实体类中

