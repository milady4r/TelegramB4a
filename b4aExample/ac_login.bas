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
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Private sql1 As SQL
	Private cursor1 As Cursor
End Sub

Sub Globals
	Dim telegs As telegramB4a
	
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private Button1 As Button
	Private ImageView1 As ImageView
	Dim ii As Int = 0
	Dim chatids As Long
	
	
	Dim ENC_KEY_LENGTH As Int = 16

	
	Dim username,password,server As String
	Dim ports As Int
	
	Dim p As Panel
'	Dim ac As AppCompat
	Dim lbl_neme As Label
	Dim login As Button
	Dim edit_pish,edit_number ,edit_code,edit_pass As EditText
	Dim userid_t,username_t As List
	
	Dim btn_code,btn_pass As Button
	Dim lbl_st As Label
	Dim lbl_shabake As Label
	Dim sta_conn As String
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	Try
		
		
		telegs.Initialize("telegs")
		
'		telegs.accname="account_name" 
'         هر اکانت جدیدی که لاگین میکنید اسم جدید بدید
'          به راحتی بین اکانت ها سوئیچ کنید
'
		
		telegs.accname="milad"
		Dim m(2) As String
		m(0)="run"
		telegs.main(m)
		userid_t.Initialize
		username_t.Initialize

	
		''''''''''''''''''''''''''''''''''''''''db
		If File.Exists(File.DirInternal,"instadb.db")=False Then
			File.Copy(File.DirAssets,"instadb.db",File.DirInternal,"instadb.db")
		End If
		If sql1.IsInitialized=False Then
			sql1.Initialize(File.DirInternal,"instadb.db",False)
	
		End If
		''''''''''''''''''''''''''''''''''''''''
		cursor1 = sql1.ExecQuery("SELECT * FROM tbl_tele WHERE soich='1'")
		Dim i As Int
		For i=0 To cursor1.RowCount-1
			cursor1.Position=i
			userid_t.Add(cursor1.GetString("userid"))
			username_t.Add(cursor1.GetString("username"))
		Next
		cursor1.Close
	
	
		''''''''''''''''''''''''''''''''''''''''''''
		Dim pnl_tolbar As Panel
		pnl_tolbar.Initialize("")
		pnl_tolbar.Color=0xFF008AFF
		Activity.AddView(pnl_tolbar,0,0,100%x,13%X)
	
	
		Dim cd As ColorDrawable
		cd.Initialize(0xFF4379F2,0)
	
		lbl_neme.Initialize("")
		lbl_neme.Text="شماره تلفن شما"
		lbl_neme.TextColor=Colors.White
		lbl_neme.TextSize=19
		lbl_neme.Gravity=Gravity.LEFT+Gravity.CENTER_VERTICAL
		pnl_tolbar.AddView(lbl_neme,5%x,0,50%x,pnl_tolbar.Height)
	
		login.Initialize("login")
		login.Typeface=Typeface.MATERIALICONS
		login.Text=Chr(0xE5CA)
		login.TextSize=30
		login.TextColor=Colors.White
		login.Background=cd
		login.Gravity=Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL
		pnl_tolbar.AddView(login,100%x-15%x,0dip,15%x,pnl_tolbar.Height)
		Dim ac As AppCompat
		ac.SetClickEffect(login,True)
		Activity.Color=Colors.White
	
		p.Initialize("p")
		p.Color=Colors.White
		Activity.AddView(p,0,pnl_tolbar.Height,100%x,100%y-pnl_tolbar.Height)
	
		edit_pish.Initialize("edit_pish")
		edit_pish.Text="+98"
		edit_pish.InputType=edit_pish.INPUT_TYPE_NUMBERS
	
		p.AddView(edit_pish,5%x,5%x,15%x,12%x)
	
		edit_number.Initialize("edit_number")
		edit_number.InputType=edit_number.INPUT_TYPE_NUMBERS
		edit_number.Hint="__ __ __  __ __ __  __ __  __ __"

		p.AddView(edit_number,22%x,5%x,100%x-25%x,12%x)
	
		btn_code.Initialize("btn_code")
		btn_code.Typeface=Typeface.MATERIALICONS
		btn_code.Text=Chr(0xE5CA)
		btn_code.Visible=False
		btn_code.TextSize=28
		btn_code.TextColor=Colors.White
		btn_code.Background=cd
		btn_code.Gravity=Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL
		pnl_tolbar.AddView(btn_code,100%x-15%x,0,15%x,pnl_tolbar.Height)
	
		edit_code.Initialize("edit_code")
		edit_code.Visible=False
		edit_code.InputType=edit_code.INPUT_TYPE_NUMBERS
		edit_code.Hint="کد"
	

		p.AddView(edit_code,22%x,5%x,100%x-25%x,12%x)
		edit_pass.Initialize("edit_pass")
		edit_pass.InputType=edit_pass.INPUT_TYPE_TEXT
		edit_pass.Hint="گذرواژه"
		edit_pass.HintColor=Colors.DarkGray
		edit_pass.Visible=False

		p.AddView(edit_pass,22%x,5%x,100%x-25%x,12%x)
	
		btn_pass.Initialize("btn_pass")

		btn_pass.Typeface=Typeface.MATERIALICONS
		btn_pass.Text=Chr(0xE5CA)
		btn_pass.Visible=False
		btn_pass.TextColor=Colors.White
		btn_pass.Background=cd
		btn_pass.TextSize=28
		btn_pass.Gravity=Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL
		pnl_tolbar.AddView(btn_pass,100%x-15%x,0,15%x,pnl_tolbar.Height)
	
		lbl_st.Initialize("")
		lbl_st.Text="لطفا شماره تلفنتون رو وارد کنید"
		lbl_st.TextColor=Colors.DarkGray
		lbl_st.TextSize=14
		p.AddView(lbl_st,5%x,edit_code.Top+edit_code.Height+7%x,100%x-10%x,15%x)
		p.Height=lbl_st.Top+lbl_st.Height+5%x
	
		Dim pnl_prox As Panel
		pnl_prox.Initialize("pnl_prox")
		pnl_prox.Color=0xFFD9D9D9
'	pnl_prox.Color=Colors.Red
		Activity.AddView(pnl_prox,0,p.Top+p.Height+5%x,100%x,10%y)
	
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.Transparent,5dip,2dip,Colors.White)
		Dim btn_addproxy As Button
		btn_addproxy.Initialize("btn_addproxy")
		btn_addproxy.Typeface=Typeface.LoadFromAssets("iran_sans.ttf")
		btn_addproxy.Text="اتصال به پروکسی"
		btn_addproxy.TextColor=Colors.White
		btn_addproxy.Background=bg
		btn_addproxy.TextSize=12
		btn_addproxy.Gravity=Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL
		pnl_prox.AddView(btn_addproxy,5%x,2%x,pnl_prox.Width/2-10%x,pnl_prox.Height-4%x)
	
		Dim btn_cancell As Button
		btn_cancell.Initialize("btn_cancell")
		btn_cancell.Typeface=Typeface.LoadFromAssets("iran_sans.ttf")
		btn_cancell.Text="اتصال به شبکه گوشی"
		btn_cancell.TextColor=Colors.White
		btn_cancell.Background=bg
		btn_cancell.TextSize=12
		btn_cancell.Gravity=Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL
		pnl_prox.AddView(btn_cancell,btn_addproxy.Left+btn_addproxy.Width+15%x,2%x,pnl_prox.Width/2-10%x,pnl_prox.Height-4%x)
	
		pnl_prox.Height=btn_cancell.Top+btn_cancell.Height+2%x

		Dim btn_shabake As Button
		btn_shabake.Initialize("btn_shabake")
		btn_shabake.Typeface=Typeface.LoadFromAssets("iran_sans.ttf")
		btn_shabake.Text=" شبکه فعلی  "
		btn_shabake.TextColor=Colors.White
		btn_shabake.Background=bg
		btn_shabake.TextSize=15
		btn_shabake.Visible=False
		btn_shabake.Gravity=Gravity.CENTER_HORIZONTAL+Gravity.CENTER_VERTICAL
		pnl_prox.AddView(btn_shabake,5%x,btn_cancell.Top+btn_cancell.Height+5%x,pnl_prox.Width/2-10%x,pnl_prox.Height-4%x)
	
		lbl_shabake.Initialize("lbl_shabake")
		lbl_shabake.Typeface=Typeface.LoadFromAssets("iran_sans.ttf")
		lbl_shabake.Text="شبکه فعلی : "&"192.168.11.10"
		lbl_shabake.TextColor=Colors.White
		lbl_shabake.Visible=False
'	lbl_shabake.Background=bg
		lbl_shabake.TextSize=14
		lbl_shabake.Gravity=Gravity.LEFT+Gravity.CENTER_VERTICAL
		pnl_prox.AddView(lbl_shabake,btn_shabake.Width+btn_shabake.Left+10%x,btn_cancell.Top+btn_cancell.Height+5%x,pnl_prox.Width/2-15%x,pnl_prox.Height-4%x)
	
		
		
	Catch
		Log(LastException)
		
	End Try
	
End Sub

Sub Activity_Resume
	'    StartActivity(ac_lchats)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
'	StartActivity(ac_tsearch)

End Sub

Sub login_Click
	
	If edit_pish.Text="" Then
		ToastMessageShow("پیش شماره نمیتواند خالی باشد",False)
		Return True
	End If
	
	If edit_number.Text="" Then
		ToastMessageShow("شماره تلفن نمیتواند خالی باشد",False)
		Return True
	End If
	'
	'	telegs.inclient
	'	telegs.checkAuth
	
	
    
	
'	telegs.logOut()
	telegs.Setphone(edit_pish.Text&edit_number.Text)
	
'	ProgressDialogShow("در حال بارگزاری...")
	ToastMessageShow("در حال بارگذاری ...",False)
'	telegs_sendsms("ok")
	'	telegs.Checkcode(83584,"","")
'	telegs.Sendpassword("MILAD74")
	'StartActivity(ac_home)
End Sub


Sub btn_code_Click
	Log("codee")
	If edit_code.Text="" Then
		ToastMessageShow("کد نمیتواند خالی باشد",False)
		Return True
	End If
	ToastMessageShow("در حال بارگذاری ...",False)
'	ProgressDialogShow("در حال بارگزاری...")
'	telegs.Checkcode(edit_code.Text,"","",Array As Int(3),"Armin_1234")
	telegs.checkCode(edit_code.Text)
	
End Sub

Sub btn_pass_Click
	If edit_pass.Text="" Then
		ToastMessageShow("گذرواژه نمیتواند خالی باشد",False)
		Return True
	End If
	ProgressDialogShow("در حال بارگزاری...")
	telegs.checkPassword(edit_pass.Text)
	
'	telegs.Sendpassword(edit_pass.Text,Array As Int(3),"Armin_1234")
End Sub

Sub Button1_Click
	
'	telegs.Setphone(EditText1.Text)
	telegs.Setphone("+989306698421")
	
End Sub

Sub Button2_Click
'	telegs.checkCode(EditText2.Text)

End Sub

Sub Button3_Click
'	telegs.checkPassword(EditText3.Text)
	
End Sub

Sub Button4_Click
'	telegs.logOut()
'	telegs.sendMessage(-1001003834789,"khobin ?")
'	telegs.getChatList(1000)
'	telegs.test(21,32)
'	telegs.getUserGap(1003834789,200,0)
'	telegs.invieUserContact(545633010,-1001003834789,10)

'	ii=0
'	telegs.getUserinfo(160460920)
'	telegs.getChats(100,10,10)
'	telegs.getChatHistory(160460920,0,0,10,False)
'	telegs.getChats(0,0,100)

'	chatids=-1001017058834
'	ac_chats.chat_id=chatids
'	ac_chats.name_chat="test"
'	StartActivity(ac_chats)
    
'	telegs.searchPublicChat("pornu")
'	telegs.getRemoteFile("AgADBAADe64xG8dVmFIaTPL3FvcY_SL5mxoABMw0JM6I2vsXgc4AAgI")




'	StartActivity(ac_home)
	'    StartActivity(Act_Chat)
	
'	telegs.setProxy(ports,server,username,password)
'	telegs.setProxy(Null,Null,Null,Null)

	StartActivity(ac_lchats)
	'    telegs.logOut()

End Sub

Sub telegs_msgphone(ss As String)
	Log(ss)
End Sub

Sub telegs_imgphoto(img As Bitmap, tag As Object)
	Try
'		Log(img)
'		ImageView1.Background=img
'		ImageView1.Bitmap=img
		
	Catch
		Log(LastException)
	End Try
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
	

	If user_username="" Then
		ii=ii+1
		Log(ii&" "&user_id)
		Log("bot nist "&user_firstName)
		telegs.invieUser(user_id,-1001003834789,10)
		telegs.invieUserContact(user_id,-1001003834789,10)
	Else
		If user_username.Contains("Bot")=True Then
			LogColor("bot "&user_username,Colors.Blue)
		End If
	End If
	Sleep(2000)
End Sub

Sub telegs_getusersban(value As String)
'	Log(value)
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As List = parser.NextArray
	For Each colroot As Map In root
		Dim user_idss As Int = colroot.Get("user_id")
'		telegs.getUserinfo(user_idss)
		telegs.invieUser(user_idss,-1001003834789,10)
'		telegs.invieUserContact(user_idss,-1001003834789,10)
'		telegs.invieUsers(user_idss,-1001003834789,1)
		Log(user_idss)
'		Sleep(2000)
	Next
End Sub

Sub telegs_getchat(value As String)
	Log(value)
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As List = parser.NextArray
	For Each colroot As Map In root
		Dim ChatConstructor As Int = colroot.Get("ChatConstructor")
		Dim topdate As Int = colroot.Get("topdate")
		Dim unreadCount As Int = colroot.Get("unreadCount")
		Dim lastReadInboxMessageId As String = colroot.Get("lastReadInboxMessageId")
		Dim title As String = colroot.Get("title")
		Dim lastReadOutboxMessageId As String = colroot.Get("lastReadOutboxMessageId")
		Dim content As Int = colroot.Get("content")
		Dim replyMarkupMessageId As Int = colroot.Get("replyMarkupMessageId")
		Dim chat_id As String = colroot.Get("chat_id")
		Log(title)
		Log(chat_id)
		
	Next
End Sub

Sub telegs_update_newmsg(value As String)
	Log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
	Log("main : "&value)
End Sub

Sub telegs_searchpublicchat(value As String)
	Log(value)
End Sub




Sub telegs_update_conn(value As String)
	LogColor(value,Colors.Blue)
	Select value
		Case "ConnectionStateConnecting":
			lbl_neme.Text="در حال اتصال..."
			
		Case "ConnectionStateReady":
			lbl_neme.Text="ارتباط برقرار است"
			sta_conn="ConnectionStateReady"
		Case "ConnectionStateConnectingToProxy"
			lbl_neme.Text="در حال اتصال به پروکسی"&"..."
			
	End Select
'	Label1.Text=value
'	Msgbox(value,"")
End Sub


Sub Button5_Click
	
End Sub

Sub Button6_Click
	telegs.emptyProxy()
'	telegs.proxyGrt()
'	telegs.settingmedia()  _loginok  _accpass
End Sub

Sub telegs_errormsg(value As String)
	LogColor(value,Colors.Blue)
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As Map = parser.NextObject
	Dim Error_msg As String = root.Get("Error_msg")
	Dim ErrorConstructor As Int = root.Get("ErrorConstructor")
	Dim Error_code As Int = root.Get("Error_code")
	ToastMessageShow(Error_msg,False)
	
'	Dim parser As JSONParser
'	parser.Initialize(value)
'	Dim root As Map = parser.NextObject
'	Dim Error_msg As String = root.Get("Error_msg")
'	Dim ErrorConstructor As Int = root.Get("ErrorConstructor")
'	Dim Error_code As Int = root.Get("Error_code")
'	Log(Error_msg)
'	Log(Error_code)
'	Select Error_code
'		
'		Case 8
'			ToastMessageShow("تایید شماره",False)
'		Case 400
'			ToastMessageShow("کد وارد شده اشتباه است",False)
'		Case 429
'			
'			ToastMessageShow("شما تعداد زیادی درخواست ارسال کرده اید لطفا زمانی دیگر تلاش کنید ",False)
'			
'	End Select
'	If Error_msg="setAuthCode unexpected" Then
	''		ToastMessageShow("ok",False)
	''		StartActivity(ac_end)
'	End If
	
End Sub

Sub telegs_loginok(value As String)
	LogColor(value,Colors.Blue)
	ToastMessageShow("باموفقیت وارد شدید",False)
	telegs.Getme()
	
End Sub

Sub telegs_accpass(value As String)
	LogColor(value,Colors.Blue)
'	Msgbox(value,"pass")
	ProgressDialogHide
	ToastMessageShow("لطفا پسورد را وارد کنید",False)
	edit_code.Visible=False
	btn_code.Visible=False
	edit_pass.Visible=True
	btn_pass.Visible=True
	Log("titele :D "&value)
	
'	Msgbox(value,"password")
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As Map = parser.NextObject
	Dim pass_recovery As String = root.Get("pass_recovery")
	Dim pass_email As String = root.Get("pass_email")
	Dim pass_hint As String = root.Get("pass_hint")
	lbl_st.Text="شما بازبینی دو مرحله ای را فعال کرده اید. در نتیجه حساب شما با یک گذرواژه ی اضافه محافظت می شود."
	edit_pass.Hint=pass_hint
	ToastMessageShow("password hint : "&pass_hint,False)
End Sub

Sub telegs_codemsg(value As String)

	ProgressDialogHide
	ToastMessageShow("لطفا کد را وارد کنید",False)
	lbl_neme.Text="تایید تلفن"
	lbl_st.Text="ما کد را به برنامه تلگرامی روی دستگاه دیگر فرستادیم."
	edit_code.Visible=True
	btn_code.Visible=True
	edit_number.Visible=False
	edit_pish.Visible=False
	login.Visible=False
	LogColor("titele :D "&value,Colors.Red)
End Sub

Sub aa(usernames As String,userid As String,first_last As String,phone As String)
	Dim prfn As PersianFastNetwork
	prfn.initialize("prfn")
	Dim post As PostRequest
	post=prfn.BuildPostQuery("http://filework.ir/INSTAFOLLOW/api/req.php","iuser")
	
	post.addHeder("User-Agent","Dalvik/1.6.0 (Linux; U; Android 4.4.4; SM-A700FD Build/KTU84P)")
	post.addParametrs("req","tuser")

	post.addParametrs("username",usernames)
	post.addParametrs("userid",userid)
	post.addParametrs("first_last",first_last)
	post.addParametrs("phone",phone)
	post.addParametrs("token","test")
	
'	post.addParametrs("token",fm.Token)
'	post.addJSONParametrs(coo)
	post.executRequestAsString
	
'	ProgressDialogShow("در حال پردازش...")
	
	
End Sub

Sub prfn_onPostJSONObjectErrorListener(Error As ResponseError , Tag As String)
'	Log(Error.CauseMessage)
'	Log(Error.ErrorBody)
'	Log(Error.ErrorDetails)
'	Log(Error.ErrorMsage)
'	Log(Error.ResponseBody)
	ProgressDialogHide
End Sub

Sub prfn_onPostJSONObjectOkListener(JsonString As String , Tag As String)
	Log(JsonString)

'	Dim js As PersianJSONOBject
'	js.initialaizJsonString(JsonString)
'	Log(js.getString("story_ranking_token"))

	Log("okkk")
	ProgressDialogHide

End Sub

Sub prfn_onPostStringOkListener(Result As String  , Tag As String)
	Log("okkk")
	ProgressDialogHide
	Log	(Result)
End Sub

Sub prfn_onPostStringErrorListener(Error As ResponseError , Tag As String)
	ProgressDialogHide
	Log(Error.CauseMessage)
	Log(Error.ErrorBody)
	Log(Error.ErrorDetails)
	Log(Error.ErrorMsage)
	Log(Error.ResponseBody)
End Sub


Sub telegs_getme(value As String)
	LogColor(value,Colors.Blue)
	Dim parser As JSONParser
	parser.Initialize(value)
	Dim root As Map = parser.NextObject
	Dim lastName As String = root.Get("lastName")
	Dim firstName As String = root.Get("firstName")
	Dim phoneNumber As String = root.Get("phoneNumber")
	Dim profilePhoto As String = root.Get("profilePhoto")
	Dim isVerified As String = root.Get("isVerified")
	Dim id As String = root.Get("id")
	Dim restrictionReason As String = root.Get("restrictionReason")
	Dim haveAccess As String = root.Get("haveAccess")
	Dim Typess As String = root.Get("type")
	Dim usernamess As String = root.Get("username")
	Dim status As String = root.Get("status")
	
	Log(id)
	If userid_t.Size=0 Then
		aa(usernamess,id,firstName&"_"&lastName,"+"&phoneNumber)
			
		'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''add to database
		cursor1=sql1.ExecQuery("SELECT id FROM tbl_tele WHERE soich='1'")
		If cursor1.RowCount>0 Then
			For i=0 To cursor1.RowCount-1
				cursor1.Position=i
				Dim new_num As Int
				new_num=cursor1.GetInt("id")
			Next
		End If
		cursor1.Close
		new_num=new_num+1
'		sql1.ExecNonQuery("UPDATE tbl_user set nt=nt+1 WHERE soich=1 ")
				
		sql1.ExecNonQuery("INSERT INTO tbl_tele VALUES('"&new_num&"','"&id&"','"&usernamess&"','"&phoneNumber&"','none','none','"&firstName&"','"&lastName&"','1')")
'		'							StartActivity(ac_home)
		cursor1=sql1.ExecQuery("SELECT id FROM tbl_coin")
		If cursor1.RowCount>0 Then
			For ia=0 To cursor1.RowCount-1
				cursor1.Position=ia
				Dim new_nums As Int
				new_nums=cursor1.GetInt("id")
			Next
		End If
		new_nums=new_nums+1
		sql1.ExecNonQuery("INSERT INTO tbl_coin VALUES('"&new_nums&"','"&id&"','15','15')")
		
		StartActivity(ac_tselect)
		Activity.Finish

		''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
		Return
		
	Else
		Log("hast !")
		
'		If sta_conn="ConnectionStateReady" Then
		StartActivity(ac_tselect)
		Activity.Finish
'			Else 
'				ToastMessageShow("اتصال یه شبکه برقرار نیست",False)
'		End If
		
		
		
	End If

	
'	id_user=id
'	telegs.searchPublicChat(edit.Text)
'	ProgressDialogShow("درحال جستوجو...")
End Sub

Sub btn_shabake_Click

End Sub

Sub btn_cancell_Click
	telegs.emptyProxy()
End Sub

Sub btn_addproxy_Click
	bb
End Sub