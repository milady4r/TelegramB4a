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
	
	Public chat_id As Long
	
End Sub

Sub Globals
	

End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	telegs.Initialize("telegs")
	telegs.getChatHistory(chat_id,00,0,50,False)
	
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub new_msg(value As String)
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
			If chat_id=meg_chatId Then
				
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
				Log("ReplyMarkup: "&msg_type)
				
				Select msg_type
					Case "message":
						Dim parser_msg As JSONParser
						parser_msg.Initialize(massege_info)
						Dim root_msg As Map = parser_msg.NextObject
						Dim msg_text As String = root_msg.Get("msg_text")
						Log(msg_text)
						
						
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
			

						
				End Select
		
				
			End If
		

			
		Next
		
	
	End If
	
End Sub


Sub telegs_getchatshistory(value As String)
	'	Log(value)
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
			LogColor(meg_id,Colors.Blue)
			
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
			
					Log("path "&photoid_topmsg)
					If photoid_path="" Then
						LogColor("in downloading",Colors.Red)
					
						telegs.downloadFile(photoid_topmsg)
						
					Else
				
					End If
				
				
			End Select
		
			
			
			Log(meg_id)
			
			
		Next
		
		
	End If

End Sub
