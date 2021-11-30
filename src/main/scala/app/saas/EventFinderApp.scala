package app.saas

import java.util.regex.Pattern
import scala.collection.mutable.ListBuffer

object EventFinderApp {
  def main(args: Array[String]): Unit = {
    val pattern = Pattern.compile("(\\d{5}).*")
    val pattern2 = Pattern.compile("(\\d{5})\\s(.*?)\\s.*")
    val pattern3 = Pattern.compile("(\\d{5})\\s(.*)")
    val lines = data.split("\n");
    val list = ListBuffer[(String, String)]()
    for (i <- 0 until lines.length) {
      val item = lines(i)
      if (item.startsWith("1")) {
        val m = pattern.matcher(item)
        if (m.find()) {
          val id = m.group(1);
          if (id == item) {
            val name = lines(i + 1)
            list.append((id, name))
          } else {
            val m2 = pattern2.matcher(item)
            if (m2.matches()) {
              val name = m2.group(2)
              list.append((id, name))
            } else {
              val m3 = pattern3.matcher(item)
              if (m3.matches()) {
                val name = m3.group(2)
                list.append((id, name))
              } else {
                println(i, item)
              }
            }
          }
        } else {
          println(i, item)
        }
      }
    }
    val ids = list.map(_._1)
    val dictIds = dic.split("\n").filter(_.nonEmpty).toSet
    val need = ids -- (dictIds)
    println(need.toList.mkString(", "))

    need.map(id=>{
      val name = list.find(_._1==id) match {
        case Some(x)=> x._2
        case _=> "xxx"
      }
      println(
        s"""
          |INSERT INTO `t_dict_meta` (`module`, `dic_id`, `dic_name`, `dic_desc`, `type`) VALUES ('SDK', '${id}', '${name}', '', 'EventID');
          |""".stripMargin.trim)
    })

  }

  def data =
    """
      |12058	初始化方法被调用	 	SDK需要缓存该事件，在getDid成功后补报(WebSDK上报)	TRANSID 	是	 	是
      |12066	SDK初始化失败	 	 	是	 	是
      |12106	初始化方法被多次调用错误	 	仅WebSDK上报	是	 	是
      |12059	start方法被app调用	 	SDK需要缓存该事件，在getDid成功后补报(WebSDK上报)	是	 	是
      |12109	start方法被sdk内部调用	 	仅WebSDK上报	是	 	是
      |12107	start方法被多次调用错误	 	仅WebSDK上报	是	 	是
      |12063	开始调用getDid方法	 	SDK需要缓存该事件，在getDid成功后补报	是	 	是
      |12078	调用getDid方法成功	 	 	是	 	是
      |12079	调用getDid方法失败	 	countly初始化参数在getDid返回，失败事件实际不会上报	是	 	是
      |12084	开始调用getConfig方法	 	 	是	是	web
      |12080	调用getConfig方法成功	 	 	是	是	web
      |12081	调用getConfig方法失败	失败错误码 HttpCode／BussinessErrorCode／CustomErrorCode	 	是	是	web
      |12117	调用303获取视博云tenantID方法	 	 	 	 	是
      |12118	调用303获取视博云tenantID成功	 	 	 	 	是
      |12119	调用303获取视博云tenantID失败	 	 	 	 	是
      |12056	FlashPlayer版本上报	FlashPlayer版本	WebSDK上报	是
      |12057	FlashPlayer版本检测失败	 	WebSDK上报	是
      | 	 	 	 	TRANSID.SEQ	是
      |10012	云玩开始启动	Android 端增加cpu型号	 	 	是	是	是
      |12020
      |加载等待框成功
      |
      | 	目前不支持自定义等待框、背景图；这四个事件暂时不上报	 	是
      |12021
      |加载等待框失败
      |
      | 	 	是
      |12022
      |加载自定义背景图成功
      |
      | 	 	是
      |12023
      |加载自定义背景图失败
      |
      | 	 	是
      |12024	片头开始预加载	 	WebSDK端 发出HEAD请求前上报	 	是
      |12025	片头预加载成功	 	WebSDK端 HEAD请求成功后前上报	 	是
      |12026	片头预加载失败	 	WebSDK端 HEAD请求失败后前上报	 	是
      |12027	开始播放片头	 	 	 	是
      |12028	播放片头成功	 	 	 	是
      |12029	播放片头失败	 	 	 	是
      |12030	开始测速	 	 	 	是
      |12031	测速完成
      |平均带宽(KB/s),最终确定的带宽(KB/s),标准差(KB/s),中位数带宽(KB/s),四分之一位带宽(KB/s),选择的清晰度id,清晰度的码率,采样数据
      |
      |选择的清晰度id,清晰度的码率，值为-1
      |
      | 	是
      |12010	测速完成，起播码率上报
      |当前码率Id:当前码率值
      |
      |废弃
      |
      | 	是
      |12032	测速失败	 	 	 	是
      |12082	开始调用getCid方法	 	 	 	是
      |12072	调用getCid方法成功	是否从session中获取：0-否 1-是，WebSDK上报该扩展参数	 	 	是
      |12073	调用getCid方法失败	失败错误码	 	 	是
      |12046	测速结果低于服务下限	当前码率Id,当前码率值,测速结果对应码率值	 	 	是
      |12088	SaaS-WS连接开始建立	 	 	 	是	是
      |12089	SaaS-WS连接成功	 	 	 	是	是
      |12090	SaaS-WS连接失败	失败错误码	 	 	是	是
      |12050	SaaS-WS连接断开	 	失败重试次数超过阀值	 	是	是
      | 	 	 	 	 	是
      |12083	开始调用getCloudService方法	 	 	 	是
      |12074	调用getCloudService方法成功	 	 	 	是
      |12075	调用getCloudService方法失败	失败错误码	 	 	是
      |12019
      |排队场景，用户选择入队
      |
      | 	 	 	是	 	web
      |10039
      |排队场景，用户选择不入队
      |
      | 	 	 	是	 	web
      | 	 	 	 	 	是
      |12044	流地址获取成功(operation == 5)	这个事件需要把流连接地址带上	RTMP山包内容：videoUrl = encode(videoUrl)	 	是	是
      |RTC上报内容：SignalUrl,CoTurnUrl,roomId = encode(SignalUrl + "," + CoTurnUrl + "," + roomId)	 	是	是
      |12085	Input连接开始建立	 	 	 	是	是
      |10021	Input连接成功	 	 	 	是	是
      |10022	Input连接失败	失败错误码	 	 	是	是
      |12049	Input连接断开	 	失败重试次数超过阀值	 	是	是
      |12086	音频流连接开始建立	 	 	 	是	是
      |12033	音频流连接成功	 	 	 	是	是
      |12035	音频流连接失败	失败错误码	 	 	是	是
      |12047	音频流连接断开	 	失败重试次数超过阀值	 	是	是
      |12087	视频流连接开始建立	 	 	 	是	是
      |12034	视频流连接成功	 	 	 	是	是
      |12039	起播码率上报	当前码率Id:当前码率值	测速起播、切换码率	 	是	是
      |12045	获取到视频第一帧	 	 	 	是	是	是
      |12036	视频流连接失败	失败错误码	 	 	是	是
      |12048	视频流连接断开	 	失败重试次数超过阀值	 	是	是
      |12011	开始自动切换码率	当前码率Id:当前码率值,目标码率Id:目标码率值,帧延迟检测周期(s),帧延迟检测冷却时间(s),延迟检测阀值(百分比),延迟检测结果(百分比)	 	 	是	是
      |12012	自动切换码率成功	当前码率Id:当前码率值,目标码率Id:目标码率值,切换耗时(ms)	收到视频第一帧	 	是	是
      |12013	自动切换码率失败	当前码率Id:当前码率值,目标码率Id:目标码率值,失败原因	 	 	是	是
      |12014	开始手动切换码率	当前码率Id:当前码率值,目标码率Id:目标码率值	 	 	是	是
      |12015	手动切换码率成功	当前码率Id:当前码率值,目标码率Id:目标码率值,切换耗时(ms)	收到视频第一帧	 	是	是
      |12016	手动切换码率失败	当前码率Id:当前码率值,目标码率Id:目标码率值,失败原因	 	 	是	是
      |10050	固定周期上报Input ping/pong时间间隔
      |ping-pong间隔时间(ms)
      |
      | 	 	是	是
      |10033
      |每ping-pong间隔达到阀值上报
      |
      |ping-pong间隔时间(ms)	为10050的子集数据	 	是	是
      |10051
      |每ping-pong间隔达到阀值，上报traceroute
      |
      |traceroute ，不包含换行、回车、tab等不可见字符
      |
      |为10050的子集数据／可能为10033的子集数据
      |
      |WebSDK不上报
      |
      | 	是	是
      |12037
      |播流期间每固定周期帧解码平均耗时
      |
      |平均解码耗时(ms)
      |
      |WebSDK不上报	 	是	是
      |12038
      |播流期间，每帧间隔时间达到阀值上报
      |
      |平均间隔(ms),平均实时延迟(ms),丢包率(百分比）
      |
      | 	 	是	是
      |12042
      |每固定周期帧延迟结果上报
      |
      |平均延迟(ms),最大延迟(ms),最大延迟发送的时间戳(ms),最小延迟(ms),最小延迟发生的时间戳(ms),[],…,[]
      |
      | 	 	是	是
      |12094	收到Input连接 startIM	 	WebSDK上报	 	是
      |12095	收到Input连接 stopIM	 	WebSDK上报	 	是
      |12116	收到Input连接非 stopIM startIM pong的消息	 	WebSDK上报	 	是
      |12001
      |点击Toolbar全屏按钮
      |
      | 	WebSDK上报	 	是	 	web
      |12002
      |点击Toolbar恢复窗口按钮
      |
      | 	WebSDK上报	 	是	 	web
      |12004	关闭自定义键位	 	废弃	 	是
      |12005	开启自定义键位	 	废弃	 	是
      |12006	点击Toolbar键盘按钮	 	WebSDK上报	 	是
      |12092	点击Toolbar“卡了”按钮	 	WebSDK上报	 	是
      |12093	点击Toolbar“不卡”按钮	 	WebSDK上报	 	是
      |12007	获取自定义按键	 	废弃	 	是
      |12008	自定义按键－恢复默认	 	WebSDK上报	 	是
      |12009	保存自定义键位	 	WebSDK上报	 	是
      |12051	自定义按键－清除全部	 	WebSDK上报	 	是
      |12052	自定义按键－设置方向键	 	WebSDK上报	 	是
      |12054	收到SaaS-WS消息
      |WS下发的: 消息类型,消息内容,消息ACK,消息MID,消息接收ID
      |
      |消息类型含义如下
      |
      |1：支付消息
      |
      |2：系统消息
      |
      |系统消息，operationCode 含义如下
      |
      |1: 排队中，显示排队人数、时长提示界面
      |
      |2:长时间未操作被踢下线
      |
      |3:云玩出错提示
      |
      |4:云玩结束提示
      |
      |5:获取到流地址
      |
      |6:询问是否进入排队
      |
      |7:排队人数过多，禁止排队
      |
      |8:实例崩溃，正在恢复
      |
      |9:正在申请实例
      |
      |10:实例已经申请到，正在准备
      |
      |11:正在刷新stoken，提示loading框
      |
      |13:恢复服务
      |
      |14:提醒用户5分钟后将会停止服务（系统维护）
      |
      |15:即将停止服务(倒数5分钟内进来的用户)
      |
      |16:提示用户已经停止服务
      |
      |21:超过申请上限
      |
      |22:服务已终止
      |
      |999:网络出错
      |
      |1000:排队中
      |
      |1001:低于服务下限
      |
      |消息详细定义参考 SaaS SDK对App的场景回调、Message接口定义
      |
      |operationCode定义参考 Saas 服务端推送提示信息
      |
      | 	是	是
      | 	 	 	 	 	是
      |12091	开始调用stopCloudService方法	 	 	 	是
      |12076	调用stopCloudService方法成功	 	 	 	是
      |12077	调用stopCloudService方法失败	 	 	 	是
      |12053	stop方法被调用	调用类型 0:App 1:SDK	 	 	是	是	是
      |13053	stop被调用errorCode上报	正常退出上报为空	 	 	是	是	是
      |12043	调用SaaS Server接口失败	 	废弃	 	是
      |12017	检测到wifi进入移动网络环境	 	只移动端上报	 	是	是	是
      |12018	检测到移动网络环境进入wifi	 	只移动端上报	 	是	是	是
      |12055	进入移动环境后，用户选择继续	 	只移动端上报	 	是	 	是
      |12040
      |播流App进入到前台
      |
      | 	只移动端上报	 	是	是	是
      |12041
      |播流App进入到后台
      |
      | 	只移动端上报	 	是	是	是
      | 	 	 	 	 	是
      |12060	瘦身是否解压so库 	需要解压(0/1)	只Android瘦身SDK上报	 	是
      |12061	瘦身解压so库结果 	是否成功(0/1),解压耗时(ms)	只Android瘦身SDK上报	 	是
      |12062	瘦身ijkplayer加载so库结果 	是否成功(0/1)	只Android瘦身SDK上报	 	是
      |12065	拷贝so库文件到sdcard	需要拷贝（0/1）	只Android瘦身SDK上报	 	是
      |12064	拷贝so库结果	是否成功(0/1)	只Android瘦身SDK上报	 	是
      | 	 	 	 	 	是
      |12096	SaaS-WS 校验失败	回调事件 onopen / onclose / onmessage	只WebSDK上报	 	是
      |12097	Input Socket校验失败	回调事件 onopen / onclose / onmessage	只WebSDK上报	 	是
      |12098	切码率过程中，Player状态校验失败
      |paused, finished, loading, playing, ready
      |
      |取值为boolean
      |
      |只WebSDK上报	 	是
      |12099	显示“加载中”界面	 	只WebSDK上报	 	是
      |12100	隐藏“加载中”界面	 	只WebSDK上报	 	是
      |12101	显示消息提示界面	消息内容	只WebSDK上报	 	是
      |12102	saas websocket 已连接,不需重连	 	只WebSDK上报	 	是
      |12103	关闭saas前一个socket连接	socket 状态码	只WebSDK上报	 	是
      |12104	sdk内部的 reconnect 事件	播放器是否关闭, 是否正在重连	只WebSDK上报	 	是
      |12105	视频流的可视帧事件	是否正在切换码流	只WebSDK上报	 	是
      |12108	flash websocket 模块初始状态	0:成功, 其他失败	只WebSDK上报	 	是
      |12110	设置阻止点击事件发送flag	true/false	只WebSDK上报	 	是
      |12111	页面关闭	报告web端的页面关闭事件	只WebSDK上报	 	是	 	web
      |12112	从get_did 接口后得到错误的countly配置信息	 	只WebSDK上报	 	是	 	web
      |12113	countly地址给修改	 	只WebSDK上报	 	是
      |12114	JS文件异步加载结束	 	-+	 	是	 	web
      |12115	开始异步文件加载	 	暂时 只 H5-SDK 上传	 	是	 	web
      |12120	开始 getCloudServiceV2 调用	streamType：[RTMP，WEBRTC]	 	 	是
      |12121	调用getCloudServiceV2 成功	streamType：[RTMP]，池化类型：PooledWithoutRestart、PooledWithRestart、NotPooled	 	 	是
      |12122	调用getCloudServiceV2 失败	失败错误码	 	 	是
      |12130	开始调用 getPublickIPV4 获取IPV4	Android SDK请求地址通过Jenkins配置请求地址HMCP_GET_IPV4_URL；	 	 	是
      |12131	获取IPV4地址成功	 	 	 	是
      |12132	获取IPV4地址失败	 	 	 	是
      | 	 	 	 	 	是
      |12200	业务逻辑内部异常	异常信息	 	 	是
      | 	 	 	 	 	是
      |12300	设备不支持	[OS, Device]	只 H5-SDK 上传	 	是
      |12301	点击下载按钮	 	只 H5-SDK 上传	 	是
      | 	 	 	 	 	是
      |12999	播流统计数据上报（APP）
      |延迟|视频字节数|最大N帧|总帧数|解码耗时|本次云玩POST失败数
      |
      |压测采样数据	 	是
      |12998	云游戏失败/异常退出上报	errorCode	 	 	是
      |12306	QoS数据上报	transId|statusCode|pts|TCCO|TCSI|TCDB|TCDE|TCRB|TCRE	statusCode 0-有效数据 1-采样超时	 	是
      | 	 	 	 	 	是
      |12500	DNS 配置	 	只Android sdk	 	是
      |12501	DNS 数据重置, 清空所有的dns cache	 	只Android sdk	 	是
      |12502	开始get dns ip 地址	 	只Android sdk	 	是
      |12503	获取 dns ipv4 地址成功	函数所花时间	只Android sdk	 	是
      |12504	获取 dns ipv6 地址成功	函数所花时间	只Android sdk	 	是
      |12505	获取 dns ip地址 失败	 	只Android sdk	 	是
      |12506	解释 dns 开始	 	只Android sdk	 	是
      |12507	解释 dns 完成	解释所花时间	只Android sdk	 	是
      |12508	优先使用ipv6地址 flag 打开	 	只Android sdk	 	是
      |12509	优先使用ipv6地址 flag 关闭	 	只Android sdk	 	是
      |12510	host/ip 连接失败	失败的 host ip 信息	只Android sdk	 	是
      |12511	websocket 地址替换成功	host , ip 等信息	只Android sdk	 	是
      |12512	rtmp 地址替换成功	host , ip 等信息	只Android sdk	 	是
      |12513
      |模块错误
      |
      |错误信息	只Android sdk	 	是
      | 	 	 	 	 	是
      |12520	websocket连接成功	socket 信息	只Android sdk	 	是
      |12521	websocket连接失败	socket 信息	只Android sdk	 	是
      | 	 	 	 	 	是
      |13001
      |手柄插拔状态上报	手柄状态，0：拔出手柄，1：插入手柄	 	 	是
      |13002	开始调用getCloudPoneInfo方法	 	 	 	 	是
      |13005	调用getCloudPoneInfo成功	 	 	 	 	是
      |13006	调用getCloudPoneInfo失败	失败错误信息	 	 	 	是
      |13003	开始调用connect方法	 	 	 	 	是
      |13007	调用connect成功	 	 	 	 	是
      |13008	调用connect失败	失败错误信息	 	 	 	是
      |13004	开始调用refresh方法	 	 	 	 	是
      |13009	调用refresh成功	 	 	 	 	是
      |13010	调用refresh失败	失败错误信息	 	 	 	是
      |13011	开始调用disconnect方法	 	 	 	 	是
      |13012	调用disconnect成功	 	 	 	 	是
      |13013	调用disconnect失败	失败错误信息	 	 	 	是
      |13014	开始调用resolution方法	 	 	 	 	是
      |13015	调用resolution成功	 	 	 	 	是
      |13016	调用resolution失败	失败错误信息	 	 	 	是
      | 	 	 	 	 	是
      |13201	连接语音通道成功	 	 	 	是
      |13202	连接语音通道失败
      |失败错误码
      | 	 	是
      |13203	收到开始录音信息
      |录音参数
      | 	 	是
      |13204	超时未收到通道成功信息（暂定10S）	 	 	 	是
      |13205	用户允许获取麦克风	 	 	 	是
      |13206	用户拒绝获取麦克风	 	 	 	是
      |13207	收到结束录音信息	 	 	 	是
      |13208	语音通道断开	 	 	 	是
      |13209	浏览器版本不支持录音功能
      |
      |
      |是
      |13210	video 视频流 获取数据成功	 	 	 	是
      |13211	video 视频流解码失败	 	 	 	是
      |13212	222接口请求开始	streamType：[RTMP，WEBRTC]	102 108 211 请求合并后接口	 	是
      |13213	222接口请求成功	streamType：[RTMP]，池化类型：PooledWithoutRestart、PooledWithRestart、NotPooled	 	 	是
      |13214	222接口请求失败	 	 	 	是
      |13215	WEBRTC开始连接	 	 	 	是
      |13216	WEBRTC连接成功	包括：webrtc自身重连成功 和 非自身重连成功。
      |
      |
      |是
      |13217	WEBRTC连接失败	 	 	 	是
      |13218	WEBRTC重试成功
      |不再使用
      |
      |Android端没有
      |
      | 	是
      |13219	WEBRTC重试失败
      |在peer被动断开后，web端再次发join进行再次的peer连接，
      |
      |10秒内没有连接成功，则为重试失败。
      |
      |Android端没有
      |
      | 	是
      |13220	WEBRTC信令服务链接超时
      |信令服务器没有连接成功
      |
      |Android端没有
      |
      |
      |
      |是
      |13221	WEBRTC发送join	 	 	 	是
      |13222	WEBRTC收到offer	 	 	 	是
      |13223	WEBRTC执行setRemoteDescription成功
      |开始调用createAnswer接口。
      |
      |等同于pc.setRemoteDescription成功
      |
      | 	是
      |13224	WEBRTC发送answer	 	等同于pc.createAnswer成功	 	是
      |13225	WEBRTC收到candidate	 	 	 	是
      |13226	WEBRTC发送candidate	 	 	 	是
      |13227	WEBRTC信令服务器已经连接再次发送join	 	Android端没有	 	是
      |13228
      |WEBRTC的client端IceConnection进入了Failed状态，且先前已连接成功
      |
      | 	 	 	是
      |13229
      |WEBRTC的client端Peer
      |iceConnectionState=disconnected
      |
      | 	 	 	是
      |13230	WEBRTC清除连接超时定时器
      |暂没使用
      |
      |Android端没有
      |
      | 	是
      |13231	WEBRTC开始重试	 	Android端没有	 	是
      |13232	WEBRTC关闭close	 	 	 	是
      |13233	WEBRTC执行接口setRemoteDescription失败
      |输出失败的message。
      |
      |执行该接口成功==13223==开始执行createAnswer
      |
      |Android端没有
      |
      | 	是
      |13234	WEBRTC创建answer成功	 	执行createAnswer成功	 	是
      |13235	WEBRTC创建answer失败
      |执行createAnswer失败
      |
      |Android端没有
      |
      | 	是
      |13236	WEBRTC执行setLocalDescription成功
      |执行createAnswer接口成功后，调用setLocalDescription
      |
      |Android端没有
      |
      | 	是
      |13237	WEBRTC执行setLocalDescription失败
      |输出失败的message。
      |
      |Android端没有
      |
      | 	是
      |13238	WEBRTC执行addIceCandidate接口成功
      |收到candidate后，调用接口pc.addIceCandidate。（IOS端没有此项）
      |
      |Android端没有
      |
      | 	是
      |13239	WEBRTC执行addIceCandidate接口失败
      |输出失败的message。（IOS端没有此项）
      |
      |Android端没有
      |
      | 	是
      |13240	WEBRTC执行ontrack，获取流信息	 	Android端没有	 	是
      |13241	WEBRTC信令服务器连接成功	 	socket连接信令服务成功	 	是
      |13242	WEBRTC信令服务器连接断开	 	socket连接信令服务断开	 	是
      |13243	WEBRTC收到onicecandidate事件中，candidate值为null	websdk、h5sdk已取消该埋点	onicecandidate事件捕获到的event.candidate值只有null时，显示该埋点	 	是
      |13244	WEBRTC不支持new RTCPeerConnection()	 	在创建对象时报错。	 	是
      |13245	信令服务状态	重连、重连成功、重连失败、重连错误	 	 	是
      |13246	sdk解码格式
      |H264,videoToolbox
      |
      | 	 	是
      |13247	网络断开 / 连上	客户端网络断开onLine=false \ 连上onLine=true。	 	 	是
      |13248
      |
      |WEBRTC收到第一帧	 	 	 	是
      |13249	WEBRTC解码完第一帧	 	 	 	是
      |13250	WEBRTC--自身重连成功	说明：webrtc如果有遇到disconnect后，会内部重试连接。从disconnect自动到connected的连接成功。	websdk 、H5SDK	 	是
      |13251	WEBRTC--其他情况下连接成功	不是webrtc自身重连成功，是其他情况下连接成功：sdk调用webrtc连接成功，peer断开(failed)后连接成功	 	 	是
      |13252	WEBRTC--peer状态	connected、disconnected、failed、close	 	 	是
      |13253	WEBRTC 收到音频信息	 	 	 	是
      |13401	connection 创建	connection 详细信息	Android/iOS webrtc 模式 有	 	是
      |13402	connection 关闭	connection 详细信息	Android/iOS webrtc 模式 有	 	是
      |13403	connection 状态变化
      |connection 详细信息
      |
      |哪个状态变化
      |
      |Android/iOS webrtc 模式 有	 	是
      |13404	connection 被选定	connection 详细信息	Android/iOS webrtc 模式 有	 	是
      |13405	connection ping	connection 详细信息	Android/iOS webrtc 模式 有	 	是
      |13406	connection ping 成功
      |connection 详细信息
      |
      |prflx addr
      |
      |Android/iOS webrtc 模式 有	 	是
      |13407	connection ping 失败
      |connection 详细信息
      |
      |错误信息
      |
      |Android/iOS webrtc 模式 有	 	是
      |13408
      |connection 收到数据
      |
      |connection 详细信息
      |
      |数据长度
      |
      |Android/iOS webrtc 模式 有	 	是
      |13409	发起 binding	port 详细信息	Android/iOS webrtc 模式 有	 	是
      |13410	binding 成功
      |port 详细信息
      |
      |srflx addr
      |
      |Android/iOS webrtc 模式 有	 	是
      |13411	binding 失败
      |port 详细信息
      |
      |错误信息
      |
      |Android/iOS webrtc 模式 有	 	是
      |13412	port 收到binding req	port 详细信息	Android/iOS webrtc 模式 有	 	是
      |13413	connection 收到binding req	connection 详细信息	Android/iOS webrtc 模式 有	 	是
      |13414	connection 处理binding req成功	connection 详细信息	Android/iOS webrtc 模式 有	 	是
      |13415	connection 处理binding req失败
      |connection 详细信息
      |
      |错误信息
      |
      |Android/iOS webrtc 模式 有	 	是
      |13416	connection ping失败断开	 	 	 	是
      |13450	peer connection断开，这个event仅记录事件，不做任何处理，真正的处理在13228	 	 	 	是
      |13451	RTC的一个connection断开后,rtc内部重连成功,仅做记录用,没有别的逻辑处理	 	 	 	是
      |13452	收到ROM端的重复OFFER,大概率是ROM端Streamer重启，或者是切换分辩率由PAAS重启了Streamer	 	 	 	是
      |13453	qos data channel 打开	 	 	 	是
      |13454	qos data 数据乱序	 	 	 	是
      |13501	ping指定域名延迟数据	ping延迟最小值|ping延迟平均值|ping延迟最大值|ping延迟偏差值(值越大说明网速越不稳定)	 	 	是
      |13502	websocket心跳超时次数	长连唯一标识（时间戳），长连连接状态，心跳超时次数	 	 	是
      |13503	websocket心跳超时次数达到最大值	长连唯一标识（时间戳），长连连接状态	 	 	是
      |13504	sdk回调app状态值status	状态值，状态值信息	 	 	是
      |13505	sdk回调app场景scene	场景值	 	 	是
      |14000	联网错误	联网错误的返回错误码及当时的请求报文	TV	 	是
      |14001	Access长连接获取输入流	获取输入流状态	TV	 	是
      |14002	Access长连接获取输出流	获取输出流状态	TV	 	是
      |14003	Access长连接发送握手协议	握手是否成功	TV	 	是
      |14004	Access长连接发送心跳	第一次发ping和第一次收到pong及从连接上到第一次发送心跳成功耗时	TV	 	是
      |14005	Access长连接下发Operation3	错误信息	TV	 	是
      |14006	Access长连接断开	断开原因	TV	 	是
      |14011	获取加密key	key值	TV	 	是
      |14012	加解密失败	加解密内容及key	TV	 	是
      |15000	云游戏结束时App主动调用上报
      |finishCode 0:正常结束 1: 超时结束 2：异常结束计入错误码（100211001）3：异常结束不计入错误码
      | 	 	是
      |14020	 	 	 	 	是
      |14021	ping空口增加埋点	 	 	 	是
      |16001	基于时钟校准的延迟检测
      |采集时间|网络耗时|解码耗时|渲染耗时|单帧耗时|采集延迟|帧大小|展示帧率|推流帧率|码率|乒乓耗时
      | 	 	是
      |16002	基于时钟校准的延迟检测 【凤凰项目】
      |采集时间|网络耗时|解码耗时|渲染耗时|单帧耗时|采集延迟|帧大小|展示帧率|推流帧率|码率|乒乓耗时
      | 	 	是
      |16003	基于时钟校准的延迟检测 - 平均值
      |采集时间|网络耗时|解码耗时|渲染耗时|单帧耗时|采集延迟|帧大小|展示帧率|推流帧率|码率|乒乓耗时|
      |感知延迟|丢包率|NACK包数量|丢包数量|从游戏输出帧到streamer采集到该帧的平均延时|
      |从游戏输出帧到streamer采集到该帧的最大延时|streamer采集帧间隔的平均时间|streamer采集帧间隔的最大时间|
      |streamer帧 从采集到encode完成 所花的平均时间|streamer帧 从采集到encode完成 所花的最大时间|streamer 的 capture fps
      | 	 	是	 	是
      |12201
      |
      |开始调用 手动释放cid	cid:将要释放实例的cid
      |开始调用根据cid立即释放实例
      |
      | 	是
      |12202	手动释放cid成功	cid:将要释放实例的cid
      |根据cid立即释放实例成功
      |
      | 	是
      |12203	手动释放cid失败	cid:将要释放实例的cid
      |根据cid立即释放实例失败
      |
      |
      |
      |是
      | 	 	 	 	 	是
      |13301	请求直播	 	 	 	是	 	是
      |13302	请求直播成功	 	 	 	是	 	是
      |13303	请求直播失败	失败错误码	 	 	是	 	是
      |13304	请求停止直播	 	 	 	是	 	是
      |13305	请求停止直播成功	 	 	 	是	 	是
      |13306	请求停止直播失败	失败错误码	 	 	是	 	是
      |13307	请求授权码	 	 	 	是
      |13308	请求授权码返回成功	 	 	 	是
      |13309	请求授权码返回失败	失败错误码	 	 	是
      |13310	请求控制权	 	 	 	是
      |13311	请求控制权成功	 	 	 	是
      |13312	请求控制权失败	失败错误码	 	 	是
      |13313	通知获得控制权成功	 	 	 	是
      |13314	通知获得控制权失败	失败错误码	 	 	是
      |13315	通知失去控制权	 	 	 	是
      |13321	互换控制权成功	 	 	 	是
      |13322	互换控制权失败	失败错误码	 	 	是
      |13323	互换控制权还原成功	 	 	 	是
      |13324	互换控制权还原失败	失败错误码	 	 	是
      |14014	量子-刷新stoken请求开始	 	 	 	是
      |14015	量子-刷新stoken请求成功	 	 	 	是
      |14016	量子-刷新stoken请求失败	 	 	 	是
      |15014	量子-确认入队请求开始	 	 	 	是
      |15015	量子-确认入队请求成功	 	 	 	是
      |15016	量子-确认入队请求失败	 	 	 	是
      |12093
      |开始调用检查是否有驻留实例方法
      |
      | 	只有iOSSDK上报	 	是
      |12094
      |调用检查是否有驻留实例方法成功
      |
      | 	只有iOSSDK上报	 	是
      |12095
      |调用检查是否有驻留实例方法失败
      |
      | 	只有iOSSDK上报	 	是
      |19000	RTCTracing方法调用后事件上报	 	只有iOSSDK上报	 	是
      | 	 	 	 	 	是
      | 	 	 	 	 	是
      |12400	screenurl  为空	 	 	 	是
      |12401	screen开始连接	 	 	 	是
      |12402	screen连接成功	 	 	 	是
      |12403	screen连接失败	 	 	 	是
      |12404	screen开始重连	 	 	 	是
      | 	 	 	 	 	是
      | 	 	 	 	 	是
      |12305	screen因无网络断开	 	 	 	是
      |12306	COUNTYLY_CONNECT_INPUT_WS_SERVER_MORE_COUNT	 	 	 	是
      |12301	Android 生命周期事件
      |pauseGame
      |restartGame
      |onRestart
      |onStart
      |onResume
      |onPause
      |onStop
      |onDestroy
      |play_bundle
      |onSwitchResolution_resId
      |onExitGame
      |release+error
      |reconnection
      |onSwitchResolution_level
      |play
      |startPlay
      | 	 	是
      | 	 	 	 	 	是
      |12299	播放状态	 	 	 	是
      | 	 	 	 	 	是
      |12140	Native初始化 _native_init	 	 	 	是
      |12141	创建Native IjkMediaPlayer 成功	 	 	 	是
      |12142	设置流地完成 _setDataSourceAndHeaders :url	 	 	 	是
      |12143	Native层开始加载解码 开始	 	 	 	是
      |12144	Native层开始加载解码 结束	 	 	 	是
      |12145	初始化帧队列 完成	 	 	 	是
      |12146	初始化数据包队列 完成	 	 	 	是
      |12147	初始化时针 完成	 	 	 	是
      |12148	创创建视频刷新的线程 完成	 	 	 	是
      |12149	创建视频读取的线程 完成	 	 	 	是
      |12150	初始化视频解码器	 	 	 	是
      |12151	开始播放 ijkmp_start	 	 	 	是
      | 	 	 	 	 	是
      |12160	FFmpeg开启输入流，并且读取header 开始 avformat_open_input	 	 	 	是
      |12161	协议读写 初始化URLContext与AVIOContext 开始 io_open_default	 	 	 	是
      |12162	开启 RTMP连接并验证流是否可以播放 rtmp_open	 	 	 	是
      |12163	开启tcp Socket连接 开始	 	 	 	是
      |12164	开启tcp Socket连接 成功:tcp://220.194.80.121:44743	 	 	 	是
      |12165	rtmp握手 开始	 	 	 	是
      |12166	rtmp握手 成功	 	 	 	是
      |12167	建立网络Connect连接，发送到Server 开始 gen_connect	 	 	 	是
      |12168	建立网络Connect连接，发送到Server 成功 gen_connect	 	 	 	是
      |12169	建立网络流 createStream 发送	 	 	 	是
      |12170	建立网络流 createStream 成功	 	 	 	是
      |12171	发送播放命令 play 开始	 	 	 	是
      |12172	发送播放命令 play 成功	 	 	 	是
      |12173	协议读写 初始化URLContext与AVIOContext 完成 ffio_open_whitelist 结束	 	 	 	是
      |12174	FFmpeg开启输入流，并读取header 结束 avformat_open_input	 	 	 	是
      | 	 	 	 	 	是
      |13316	游戏退出	 	 	 	是
      | 	 	 	 	 	是
      |13506	无网络	 	 	 	是
      | 	 	 	 	 	是
      |13701	获取测速信息	 	 	 	是
      |13702	获取测速信息成功	 	 	 	是
      |13703	获取测速信息失败	 	 	 	是
      |13704	测速状态
      |测试状态
      |* start，开始
      |* prepare，准备
      |* running，运行中
      |* stop，停止。app停止：stop,1；sdk停止：stop,0,time out(停止的场景信息或者停止的原因)
      |* finish，结束
      |* error，错误。eg：error,time out(错误信息)
      |
      | 	 	是
      |12092	切换分辨率失败	 	 	 	是
      |12621	rom侧分享通知sdk	 	 	 	是
      |12622	rom侧打开相册通知sdk	 	 	 	是
      |12623	rom侧打开相册通知sdk	 	 	 	是
      |12601	开始上传	 	 	 	是
      |12602	单文件上传成功	 	 	 	是
      |12603	上传停止	{"message":"xxxx"}	message为上传停止的原因描述	 	是
      |12604	取消上传	 	 	 	是
      |12605	上传错误	{"message":"xxxx"}	message为上传错误的原因描述	 	是
      |12606	上传完成	 	 	 	是
      |12607	上传状态
      |{
      |"file_segment_coefficient": "xxxx",
      |"file_segment_size": "xxxx",
      |"file_size": "xxxx",
      |"upload_cost": "xxxx"
      |}
      |
      |file_segment_coefficient：上传文件片系数
      |
      |file_segment_size：上传文件片大小，单位字节
      |
      |file_size：文件大小，单位字节
      |
      |upload_cost：文件上传花费时间，单位毫秒
      |
      | 	是
      |12608	切换分辨率关闭 上传长连接	 	 	 	是
      |12230
      |
      |saas返回指令格式不正确
      |
      | 	 	 	是
      |12232
      |
      |201请求返回参数不正确
      |
      |{
      |
      |“eventDataVer”:”1.0”
      |
      |    "errorCode": “xxxx”,
      |
      |    "errorMsg": “xxxx”，
      |
      |    "httpStatusCode": “502,404",
      |
      |    "httpMsg": “xxxx”,
      |
      |     timeoutMS:”10000”，
      |
      |}
      |
      | 	 	是
      |12233
      |
      |准备游戏过程中参数校验失败
      |
      |{
      |
      |“eventDataVer”:”1.0”
      |
      |    "errorCode": “xxxx”,
      |
      |    "errorMsg": “xxxx”，
      |
      |    "httpStatusCode": “502,404",
      |
      |    "httpMsg": “xxxx”,
      |
      |     timeoutMS:”10000”，
      |
      |}
      |
      | 	 	是
      |12234
      |
      |因为什么原因导致没有上报16003事件
      |
      |每种原因只上报一次，什么原因导致没有上报例各端无需保持一致如：playerIsNull，netDelayAverageIsZero，lastRealPing2pongCostTimeIsErr
      |
      | 	 	是
      |12235
      |
      |用户点击play没有达到请求211标准
      |
      |{
      |
      |“eventDataVer”:”1.0”
      |
      |    "errorCode": “xxxx”,
      |
      |    "errorMsg": “xxxx”，
      |
      |    "httpStatusCode": “502,404",
      |
      |    "httpMsg": “xxxx”,
      |
      |     timeoutMS:”10000”，
      |
      |}
      |
      | 	 	是
      |13800
      |返回流类型不一致，开始切流
      | 	 	 	是
      |13801	226请求成功	 	 	 	是
      |13802
      |226获取流地址成功
      | 	 	 	是
      |13803	226获取流地址失败或者请求失败	 	 	 	是
      |12901	向WsServer发送数据	{"message":"xxxx"}	 	 	是
      |12902	向WsServer发送数据成功	{"message":"xxxx"}	 	 	是
      |12903	向WsServer发送数据失败	{"message":"xxxx"}	 	 	是
      |12904	接收到WsServer数据	{"message":"xxxx"}
      |""".stripMargin

  def dic =
    """
      |20001
      |20002
      |20003
      |20004
      |20005
      |20006
      |20007
      |20008
      |20009
      |20010
      |20011
      |20012
      |20013
      |20014
      |20015
      |20016
      |20017
      |20018
      |20019
      |20020
      |10011
      |100110
      |100112
      |100113
      |100114
      |100115
      |100116
      |100117
      |100118
      |100121
      |100122
      |100123
      |100124
      |100125
      |100126
      |100127
      |100128
      |100129
      |10013
      |100130
      |100131
      |100132
      |100133
      |100134
      |100135
      |100136
      |100137
      |100138
      |100139
      |10014
      |100140
      |100141
      |100142
      |100143
      |100144
      |100145
      |100146
      |100147
      |100148
      |100149
      |10015
      |10016
      |10017
      |10018
      |10023
      |10027
      |10031
      |10034
      |10035
      |10036
      |10052
      |10053
      |10054
      |10055
      |10056
      |10057
      |10058
      |10059
      |10060
      |10061
      |10062
      |100621
      |100622
      |100623
      |10063
      |10064
      |10065
      |10066
      |100661
      |100662
      |100663
      |100664
      |100665
      |100666
      |100667
      |100668
      |100669
      |100670
      |10069
      |10070
      |10071
      |10072
      |10073
      |10074
      |10075
      |10076
      |10077
      |10078
      |10079
      |10080
      |10081
      |10082
      |10083
      |10084
      |10086
      |10087
      |10088
      |10089
      |10090
      |10091
      |10092
      |10093
      |10094
      |10095
      |10096
      |10097
      |10098
      |10099
      |10100
      |10101
      |10102
      |10103
      |10104
      |10105
      |10106
      |10107
      |10108
      |10109
      |10110
      |10112
      |10012
      |10021
      |10022
      |10033
      |10050
      |10051
      |12001
      |12002
      |12006
      |12008
      |12009
      |12011
      |12012
      |12013
      |12014
      |12015
      |12016
      |12017
      |12018
      |12020
      |12024
      |12025
      |12027
      |12028
      |12030
      |12031
      |12033
      |12034
      |12035
      |12036
      |12037
      |12038
      |12039
      |12040
      |12041
      |12044
      |12045
      |12046
      |12049
      |12050
      |12051
      |12052
      |12053
      |12054
      |12055
      |12056
      |12057
      |12058
      |12059
      |12063
      |12072
      |12074
      |12075
      |12076
      |12077
      |12078
      |12079
      |12080
      |12081
      |12082
      |12083
      |12084
      |12085
      |12086
      |12087
      |12088
      |12089
      |12090
      |12091
      |12094
      |12095
      |12097
      |12098
      |12099
      |12100
      |12101
      |12102
      |12103
      |12104
      |12105
      |12106
      |12108
      |12109
      |12110
      |12111
      |12112
      |12114
      |12115
      |12120
      |12121
      |12122
      |12200
      |12301
      |12302
      |12303
      |12304
      |12401
      |12402
      |12403
      |12404
      |13001
      |13002
      |13003
      |13004
      |13005
      |13006
      |13007
      |13008
      |13009
      |13010
      |13011
      |13012
      |13013
      |13014
      |13015
      |13016
      |13053
      |13210
      |13212
      |13213
      |13215
      |13216
      |13221
      |13222
      |13223
      |13224
      |13225
      |13226
      |13228
      |13229
      |13230
      |13232
      |13236
      |13238
      |13240
      |13241
      |13242
      |16000
      |16001
      |16003
      |30000
      |30001
      |30002
      |30003
      |30004
      |30005
      |30006
      |30007
      |30008
      |30009
      |30010
      |30011
      |30012
      |30013
      |30014
      |30015
      |30016
      |30017
      |30018
      |30019
      |30020
      |30021
      |30022
      |31000
      |31001
      |31002
      |31003
      |21000
      |21001
      |21002
      |21003
      |21010
      |21011
      |21012
      |21013
      |21014
      |21015
      |21016
      |21017
      |21018
      |21019
      |21020
      |21021
      |21022
      |21023
      |""".stripMargin
}
