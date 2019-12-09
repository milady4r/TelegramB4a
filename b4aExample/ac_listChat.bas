B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=9.5
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	Dim telegs As telegramB4a

End Sub

Sub Globals
	
	Dim list_chats(30) As List
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	For i=0 To 28
		list_chats(i).Initialize
	Next
	
	telegs.Initialize("telegs")
	telegs.getChatList(50)
	

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub telegs_getchat(value As String)
	LogColor("chats : "&value,Colors.Red)
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As List = parser.NextArray
	For Each colroot As Map In root
		Dim photosmall_id As Int
		If colroot.Get("photosmall_id")=Null Then
		Else
			photosmall_id	=colroot.Get("photosmall_id")
		End If
		Dim chat_id As Long = colroot.Get("chat_id")
		Dim title As String = colroot.Get("title")
		Dim ChatConstructor As String = colroot.Get("ChatConstructor")
		Dim topdate As String = colroot.Get("topdate")
		Dim chat_type As String = colroot.Get("chat_type")
		Dim channel_info As String = colroot.Get("channel_info")
		Dim group_info As String = colroot.Get("group_info")
		Dim privatechat_info As String = colroot.Get("privatechat_info") '
		Dim replyToMessageId As String = colroot.Get("replyToMessageId")
		Dim lastReadInboxMessageId As String = colroot.Get("lastReadInboxMessageId")
		Dim lastReadOutboxMessageId As String = colroot.Get("lastReadOutboxMessageId")
		Dim unreadCount As String = colroot.Get("unreadCount")
            
		Log("chat : "&title&"reply :"& chat_type&" lastin : "&lastReadInboxMessageId&" lastout : "&lastReadOutboxMessageId&" unread : "&unreadCount)
		
		Dim content As Long = colroot.Get("content")
		Dim topmasg_type As String = colroot.Get("topmasg_type")
		Dim topmsg_info As String = colroot.Get("topmsg_info")

		telegs.downloadFile(photosmall_id)
		list_chats(1).Add(photosmall_id)
		list_chats(2).Add(chat_id)
		list_chats(3).Add(title)
		list_chats(4).add(ChatConstructor)
		list_chats(5).Add(topdate)
		list_chats(6).Add(chat_type) 'channel_info
		list_chats(7).Add(channel_info) 'channel_info
		list_chats(8).Add(group_info)  'group info
		list_chats(9).Add(privatechat_info)  'group info
		list_chats(10).Add(topmasg_type)  'topmasg_type
		list_chats(11).Add(topmsg_info)  'topmsg_info content
		list_chats(12).Add(content)  'message content
		list_chats(16).Add(unreadCount)  'message unread
		
		
	Next
		
	
	
End Sub

Sub telegs_errormsg(value As String)
	ProgressDialogHide
	Log("error :D "&value)
	'	Msgbox(value,"error")
	'	Dim parser As JSONParser
	'	parser.Initialize(value)
	'	Dim root As Map = parser.NextObject
	'	Dim Error_msg As String = root.Get("Error_msg")
	'	Dim ErrorConstructor As Int = root.Get("ErrorConstructor")
	'	Dim Error_code As Int = root.Get("Error_code")
	'
	'	If Error_msg="setAuthCode unexpected" Then
	'		ToastMessageShow("ok",False)
	'		'		StartActivity(ac_end)
	'	End If
End Sub