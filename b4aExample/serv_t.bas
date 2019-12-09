B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=9.3
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: True
	
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
'	Dim telegs As telegramB4a
	Dim tele As telegramB4a

End Sub

Sub Service_Create
	
	tele.Initialize("telegs")
	tele.accname="milad"
	Dim m(2) As String
	m(0)="run"
	tele.main(m)
	
End Sub

Sub Service_Start (StartingIntent As Intent)
      tele.Getme()
	  Log("get me")
End Sub

Sub Service_Destroy

End Sub

Sub telegs_update_conn(value As String)
	LogColor("service : "&value,Colors.Blue)

End Sub

Sub telegs_getuserinfo(value As String)
'	Log(value)
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As Map = parser.NextObject
	Dim user_bot As String = root.Get("user_bot")
	Dim user_id As Int = root.Get("user_id")
	Dim user_phoneNumber As String = root.Get("user_phoneNumber")
	Dim user_username As String = root.Get("user_username")
	Dim user_persistentId As String = root.Get("user_persistentId")
	Dim user_lastName As String = root.Get("user_lastName ")
	Dim user_photoid As Int = root.Get("user_photoid")
	Dim user_firstName As String = root.Get("user_firstName ")
	
	Log(user_phoneNumber)

	Sleep(2000)
End Sub

Sub telegs_update_newmsg(value As String)
	Log("appname : "&tele.accname)
	tele.Getme()
	
	tele.accname="files"
	Dim m(2) As String
	m(0)="run"
	tele.main(m)
	
	LogColor("service : "&value,Colors.Red)
	CallSub2(ac_getChatHistory,"new_msg",value)

	If value ="" Then
		Return
	Else
		Log("chats ready!")
		Dim parser As JSONParser
		parser.Initialize(value)
		Dim root As List = parser.NextArray
		For Each colroot As Map In root
			Dim meg_replyToMessageId As Int = colroot.Get("meg_replyToMessageId")
			Dim meg_chatId As Long = colroot.Get("meg_chatId")
'			If chat_id=meg_chatId Then
				
			Dim meg_senderUserId As Int = colroot.Get("meg_senderUserId")
			Dim meg_editDate As Int = colroot.Get("meg_editDate")
			Dim meg_isPost As String = colroot.Get("meg_isPost")
			Dim meg_id As Long = colroot.Get("meg_id")
'			Log(meg_id)
			Dim meg_views As Int = colroot.Get("meg_views")
			Dim msg_type As String = colroot.Get("msg_type")
			Dim meg_canBeDeleted As String = colroot.Get("meg_canBeDeleted")
			Dim massege_info As String = colroot.Get("massege_info")
			Dim meg_viaBotUserId As Int = colroot.Get("meg_viaBotUserId")
			Dim meg_date As Int = colroot.Get("meg_date")
			Dim meg_canBeEdited As String = colroot.Get("meg_canBeEdited")
			Dim ReplyMarkup As String = colroot.Get("ReplyMarkup")
			Log("ReplyMarkup: "&ReplyMarkup)
				
			Select msg_type
				Case "message":
					Dim parser_msg As JSONParser
					parser_msg.Initialize(massege_info)
					Dim root_msg As Map = parser_msg.NextObject
					Dim msg_text As String = root_msg.Get("msg_text")

						
				Case "photo":
					Log("photo")
					Dim parser_photo As JSONParser
					parser_photo.Initialize(massege_info)
					Dim root_photo As Map = parser_photo.NextObject
					Dim photoid_isDownloadingCompleted As String = root_photo.Get("photoid_isDownloadingCompleted")
					Dim photoid_topmsg As Int = root_photo.Get("photoid_topmsg")
					Dim photoid_downloadedPrefixSize As Int = root_photo.Get("photoid_downloadedPrefixSize")
					Dim caption_topmsg As String = root_photo.Get("caption_topmsg")
					Dim photoid_path As String = root_photo.Get("photoid_path")
					Dim photoid_downloadedSize As Int = root_photo.Get("photoid_downloadedSize")
					Dim photoid_height As Int = root_photo.Get("photoid_height")
					Dim photoid_type As String = root_photo.Get("photoid_type")
					Dim photoid_size As Int = root_photo.Get("photoid_size")
					Dim caption_id As String = root_photo.Get("caption_id")
					Dim photoid_width As Int = root_photo.Get("photoid_width")
					Dim photoid_isDownloadingActive As String = root_photo.Get("photoid_isDownloadingActive")
					Dim photoid_remote As String = root_photo.Get("photoid_remote")
					Dim photoid_canBeDownloaded As String = root_photo.Get("photoid_canBeDownloaded")
					Dim photoid_canBeDeleted As String = root_photo.Get("photoid_canBeDeleted")
			
					Log("id can download "&photoid_canBeDownloaded)
					Log("size dn "&photoid_downloadedPrefixSize)
					Log("path "&photoid_path)
					If photoid_path="" Then
						LogColor("in downloading",Colors.Red)
	
					Else

					End If


						
			End Select
		
				
'			End If
		

			
		Next
		
	
	End If
	
End Sub

Sub telegs_imgaddress(respon As String)
'								Wait For telegs_imgaddress(value As String)
	LogColor("imf :"&respon,Colors.Red)
					
End Sub

Sub telegs_update_file(respon As String)
	LogColor("imf :"&respon,Colors.Red)
	
End Sub
