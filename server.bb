;---------------------------------------------------------;
;------------------- SERVER INITIALIZE ------------------- 

Const GAME_SOLO%=0
Const GAME_SERVER%=1
Const GAME_CLIENT%=2

Const PLAYER_CLASSD%=0
Const PLAYER_MENTAL%=1
Const PLAYER_REDMIST%=2
Const PLAYER_EYEKILLER%=3

Const RIGHTS_ADMIN%=0
Const RIGHTS_MONSTER%=1
Const RIGHTS_CLASSD%=2
Const RIGHTS_HOST%=3

Const SERVER_MESSAGE_DISCONNECT% = 100000
Const SERVER_MESSAGE_KICK%		 = 100001

Global AUTO_DISCONNECT_TIME = Int(GetINIInt("options.ini","multiplayer","autodisconnect time")) * 1000
Global SCARE_COOLDOWN = Int(GetINIInt("options.ini","multiplayer","scare cooldown")) * 60
Global MessageSendToServer$ = ""

Global flooramount = 210
Dim FloorActions(flooramount)
Dim FloorTimer(flooramount)

Global isFlying% = False
Global UpdateTimer = 0
;Global ChatLogFile
Dim HorrorSFX(9)

Global ID = 0
Const UDP_FREQUENCY = 20

Global name$ = GetINIString("options.ini","multiplayer","name")

Global Client_HighPacketLose = False
Global Server%,Stream%,PlayState%,IncomingStream%,PlayerCount%,PlayerID%,UDPSend%,LastMsgID%,SentMsgID%
Global PlayerName$,ServerAddress$
ServerAddress$ = GetINIString("options.ini","multiplayer","ip")
Global SentChatMsg$,SentChatMsgID%
Global TypedChatMsg$,ChatOpen%
Global AdminSpyChat% = 0
Global RecvChatID%
Global MsgCount = 0

Global PlayingAs%
Global SpookTimer%,SpookCooldown%,SpookCount%,VisibilityTimer%,LastRecvSpook%,LastSentSpook%

Global AnimStandTime = 301

LastMsgID = 0

Global MoveForw%,MoveBack%,MoveLeft%,MoveRight%,MoveShift%

Global DirListShift%
Global DirList%[60]
Global DirListAngle#[60]
Global MemX#[60]
Global MemY#[60]
Global MemZ#[60]

Type ChatMessage
	Field Txt$
	Field R%,G%,B%
	Field ID%
	Field Timer%
	Field SendTo%[32]
	Field Sender%
End Type

PlayerCount = 0
PlayerID = 0
PlayState = GAME_SOLO
UDPSend = 0
PlayerName$ = GetINIString("options.ini","multiplayer","name")
Global Rights% = RIGHTS_CLASSD
Global Act% = 0

Type Player
	Field IP%,Port%,Pivot%
	Field FallSpeed#
	Field MLeft%,MRight%,MForw%,MBack%,MClick1%,MClick2%,MShift%
	Field Name$
	Field DirList%[60]
	Field DirListAngle#[60]
	Field DirsToRead%
	Field LastMsgTime%,LastClientMsg%
	Field TexturePath$
	Field LoadedTexture%
	Field LastChatMsgID%
	Field LastRecvChatMsg%
	Field Connected%
	Field PlayingAs%
	Field SpookCount%
	Field SpookCooldown%
	Field VisibilityTimer%
	Field VisibleTimer%
	Field LastSpookSent%
	Field IsVisible%
	Field KillTimer%
	Field Stamina#
	Field Rights%
	Field Act%
	Field InView%
	Field IsFlying%
	Field Red%
	Field Green%
	Field Blue%
End Type

Dim Players.Player(60)
Dim FloorEntities%(1)
Global PlayerMesh = LoadAnimMesh("GFX\npc\mental.b3d")
Global PlayerTex = LoadTexture("GFX\npc\player.jpg")

; --------------------- SERVER INITIALIZE END ---------------------

Function ConnectToServer%(Addr$="")
	Server% = CreateUDPStream() ;create a UDP stream with any free port
	
	Local waitForCo% = MilliSecs()+10000
	
	TempStr$ = ""
	
	xcenter = GraphicsWidth()/2
	ycenter = GraphicsHeight()/2
	ticks = 0
	
	If (Not Server) Then
		PlayState = GAME_SOLO
	Else
		WriteLine Server,"connected" +GAME_VERSION
		WriteByte Server,0
		WriteLine Server,PlayerName
		CountHostIPs(Addr) ;get host ips from server
		SendUDPMsg(Server,HostIP(1),8730)
		Stream = RecvUDPMsg(Server)
		Local prevSendTime% = MilliSecs()
		While Not Stream
			SetBuffer BackBuffer()
			Stream = RecvUDPMsg(Server)
			Cls
			Color 255,255,255
			Text xcenter,ycenter,"Trying to connect to the server...",True
			Color 30,30,30
			If(Button(xcenter-30,ycenter+40,60,25,"Cancel",False)) Then Return
			Flip
			Delay 100
			ticks = ticks + 1
			If MilliSecs()>prevSendTime+3000 Then
				WriteLine Server,"connected" +GAME_VERSION
				WriteByte Server,0
				WriteLine Server,PlayerName
				SendUDPMsg(Server,HostIP(1),8730)
				prevSendTime% = MilliSecs()
			EndIf
		Wend
		
		Color 255,255,255
		Cls
		Text xcenter,ycenter,"Joining...",True
		Color 30,30,30
		If(Button(xcenter-30,ycenter+40,60,25,"Cancel",False)) Then Return
		Flip
		
		PlayState=GAME_CLIENT ;let the game know you're a client
		Players(0)=New Player
		Players(0)\IP = UDPMsgIP(Server)
		Players(0)\Port = UDPMsgPort(Server) ;save the server's information
		Players(0)\LastMsgTime = MilliSecs()
		MapString = ReadLine(Server)
		ID = ReadByte(Server)
		If MapString = "KICK" Or Mid(MapString,1,7) = "version" Then
			PlayState = GAME_SOLO
			CloseUDPStream(Server)
			Delete Players(0)
			
			Cls
			
			While Not Button(xcenter-30,ycenter+40,60,25,"Back",False)
				
				Color 255,255,255
				If Mid(MapString,1,7) = "version" Then
					Text xcenter,ycenter,"The server runs on version " + Mid(MapString,8,Len(MapString)-7)+ "."
				ElseIf MapString = "KICK" Then
					Text xcenter,ycenter,"You have been kicked from the server."
				EndIf
				Color 30,30,30
				
				Flip
				Delay 100
			Wend
			Return
		EndIf
		SCARE_COOLDOWN = ReadInt(Server)
		
		ticks = 0
		Local map$ = "0"
		While map <> "1"
			WriteLine Server,"map" +MapString
			WriteByte Server,0
			WriteLine Server,PlayerName
			SendUDPMsg(Server,Players(0)\IP,Players(0)\Port)
			
			Color 255,255,255
			Text xcenter,ycenter,"Joining...",True
			Color 30,30,30
			If(Button(xcenter-30,ycenter+40,60,25,"Cancel",False)) Then Return
			Flip
			
			Delay 200
			ticks = ticks + 1
			getconn = RecvUDPMsg(Server)
			map$ = ReadLine(Server)
			DebugLog map$
			If map <> "1" Then MapString = map
			
			If ticks > 10 And map = "0" Then
				CloseUDPStream(Server)
				Cls
				Color 30,30,30
				While Not Button(xcenter-30,ycenter+40,60,25,"Back",False)
					
					Color 255,255,255
					Text xcenter,ycenter,"Connection lost.",True
					Color 30,30,30
					
					Flip
					Delay 100
				Wend
				Return
			EndIf
			
			If ticks > 50 Then
				CloseUDPStream(Server)
				Cls
				Color 30,30,30
				While Not Button(xcenter-30,ycenter+40,60,25,"Back",False)
					
					Color 255,255,255
					Text xcenter,ycenter,"Could not join due to high packet lose.",True
					Color 30,30,30
					
					Flip
					Delay 100
				Wend
				Return
			EndIf
		Wend
	EndIf

End Function

Function LoadMap()
	;The First Area rooms
	map0 = LoadMesh("GFX\map0.x")   ; First room. The player must proceed to the 6xt floor to begin
	;GetMentalRoomMesh(map0)
	map = LoadMesh("GFX\map.x")     ; Default staircase hallway. You will cross through many of these; 087-B-1 may appear at those
	map1 = LoadMesh("GFX\map1.x")   ; Room with an window where 087-B-2 may appear
	map2 = LoadMesh("GFX\map2.x")   ; Room where 087-B-2 may appear
	map3 = LoadMesh("GFX\map3.x")   ; Big room with an stair leading to the lower floor
	map4 = LoadMesh("GFX\map4.x")
	map5 = LoadMesh("GFX\map5.x")	
	map6 = LoadMesh("GFX\map6.x")	; Room with 3 hallways leading to the same place
	map7 = LoadMesh("GFX\map7.x")	; Small room with column
	map8 = LoadMesh("GFX\map8.x")	; Big room with pit in center
	map9 = LoadMesh("GFX\map9.x")	; Hallway with many turns
	map10 = LoadMesh("GFX\map10.x")	; Hallway with many turns
	map11 = LoadMesh("GFX\map11.x")	; ACT_WAIT
	map12 = LoadMesh("GFX\map12.x")	; Big room with many pits
	map13 = LoadMesh("GFX\map13.x")	; Long hallway, also ACT_BEHIND
	map14 = LoadMesh("GFX\map14.x") ; Hallway with window
	map15 = LoadMesh("GFX\map15.x") ; Hallway with upstairs and downstairs
	map16 = LoadMesh("GFX\map16.x") ; ACT_BIGSTAIRSROOM
	map17 = LoadMesh("GFX\map17.x") ; ACT_BLUR
	map18 = LoadMesh("GFX\map18.x") ; Maze
	map19 = LoadMesh("GFX\map19.x") ; Room with window
	map20 = LoadMesh("GFX\map20.x") ; Room with broken wall
	map21 = LoadMesh("GFX\map21.x") ; Room with broken wall 2
	map22 = LoadMesh("GFX\map22.x") ; ACT_173_2
	map23 = LoadMesh("GFX\map23.x") ; Unknown Event
	map24 = LoadMesh("GFX\ending.x")
	map26 = LoadMesh("GFX\map26.x") ; ACT_RUN_2, ACT_RUN_3
	map27 = LoadMesh("GFX\map27.x") ; Hallway with window
	map28 = LoadMesh("GFX\map28.x") ; Cross
	map_maze = LoadMesh("GFX\act_maze.x") ; ACT_MAZE
	elevator = LoadMesh("GFX\elevator.x") ; ACT_WAIT
	mapending = LoadMesh("GFX\mapending.x")
	mapnothing = LoadMesh("GFX\mapnothing.x")
	map_transition = LoadMesh("GFX\map_a1_transition.x")
	map_elevator = LoadMesh("GFX\map_elevator.x")
	map_elevator_end = LoadMesh("GFX\map_elevator_end.x")
	
	;Create door
	door = CreateCube()
	ScaleEntity(door, 0.5,1,0.5)
	EntityType door, hit_map
	PositionEntity(door, -3.5, -1, -0.5)
	
	doortexture = LoadTexture("GFX\map\door.jpg")
	EntityTexture(door, doortexture)
	
	label = CreateCube()
	ScaleEntity(label, 0.35,0.35,0.35)
	EntityType label, hit_map
	PositionEntity(label, 0.34, -0.7, -1.49)
	
	labeltexture = LoadTexture("GFX\map\label.jpg")
	EntityTexture(label, labeltexture)
		
	SetFont font 
	
	Firstfloor = 0

	For i = Firstfloor To Len(MapString)/2
		Select Mid(MapString,i*2+1, 2)
			Case "00":
				CreateFloor(map0,i)
			Case "99":
				CreateFloor(map,i)
			Case "01":
				CreateFloor(map1,i)
			Case "02":
				CreateFloor(map2,i)
			Case "03":
				CreateFloor(map3,i)
			Case "04":
				CreateFloor(map4,i)
			Case "05":
				CreateFloor(map5,i)
			Case "06":
				CreateFloor(map6,i)
			Case "07":
				CreateFloor(map7,i)
			Case "08":
				CreateFloor(map8,i)
			Case "09":
				CreateFloor(map9,i)
			Case "10":
				CreateFloor(map10,i)
			Case "11":
				CreateFloor(map11,i)
			Case "12":
				CreateFloor(map12,i)
			Case "13":
				CreateFloor(map13,i)
			Case "14":
				CreateFloor(map14,i)
			Case "15":
				CreateFloor(map15,i)
			Case "16":
				CreateFloor(map16,i)
			Case "17":
				CreateFloor(map17,i)
			Case "18":
				CreateFloor(map18,i)
			Case "19":
				CreateFloor(map19,i)
			Case "20":
				CreateFloor(map20,i)
			Case "21":
				CreateFloor(map21,i)
			Case "23"
				CreateFloor(map23,i)
			Case "26":
				CreateFloor(map26,i)
			Case "27":
				CreateFloor(map27,i)
			Case "28":
				CreateFloor(map28,i)
			Case "22":
				CreateFloor(map22,i)
				
			Case "85":
				CreateFloor(map_elevator_end,i)
			Case "84":
				CreateFloor(map_transition,i)
			Case "83":
				CreateFloor(mapnothing,i)
			Case "82":
				CreateFloor(mapending,i)
			Case "81":
				CreateFloor(map_maze,i)
			Case "80":
				CreateFloor(elevator,i)
			Default:
				CreateFloor(map,i)
		End Select
	Next
	
	For i = 0 To flooramount-1
		flooractions(i) = 0
		floortimer(i) = 0
	Next

	SetFont font1

	SetBuffer BackBuffer() 	
	
	FreeEntity map0
	FreeEntity map
	FreeEntity map1
	FreeEntity map2
	FreeEntity map3
	FreeEntity map4
	FreeEntity map5
	FreeEntity map6
	FreeEntity map7
	FreeEntity map8
	FreeEntity map9
	FreeEntity map10
	FreeEntity map11
	FreeEntity map12
	FreeEntity map13
	FreeEntity map14
	FreeEntity map15
	FreeEntity map16
	FreeEntity map17
	FreeEntity map18
	FreeEntity map19
	FreeEntity map20
	FreeEntity map21
	FreeEntity map22
	FreeEntity map23
	FreeEntity map24
	FreeEntity map26
	FreeEntity map27
	FreeEntity map28
	FreeEntity map_maze
	FreeEntity mapending
	FreeEntity elevator
	FreeEntity mapnothing
	FreeEntity map_transition
	FreeEntity map_elevator
	FreeEntity map_elevator_end

End Function

Function JoinServer%(Addr$="")
	LoadMap()
	Local waitForCo% = MilliSecs()+10000
	
	TempStr$ = ""
	
	ticks = 0
	
	If (Not Server) Then
		PlayState = GAME_SOLO
	Else
		PlayState=GAME_CLIENT ;let the game know you're a client
		Rights = RIGHTS_CLASSD
		CurrEnemy = Null
			
			;collider = CreatePivot() ;reset erased objects
			PlayerTex = LoadTexture("GFX\npc\player.jpg")
			For j=0 To 31
				If Players(j)=Null Then Players(j)=New Player : Players(j)\Connected = False
				Local TmpMesh = LoadAnimMesh("GFX\npc\mental.b3d")
				EntityTexture TmpMesh,PlayerTex
				Players(j)\Pivot = CreatePivot()
				PositionEntity Players(j)\Pivot,-200.5,ColliderDefaultY#,-0.5
				EntityRadius Players(j)\Pivot, ColliderXRadius#,ColliderYRadius#
				EntityParent TmpMesh,Players(j)\Pivot
				ScaleEntity TmpMesh, 0.15,0.15,0.15 
				PositionEntity TmpMesh,-200.5,-0.5,True
				EntityAlpha GetChild(Players(j)\Pivot,1),0.0
				EntityType Players(j)\Pivot,hit_friendly

			Next
			
			PositionEntity collider,-2.5, ColliderDefaultY#, -0.5
			EntityType collider, hit_friendly
	EndIf

	UpdateLights()
End Function

Function SendToServer(tempstr$="")
	If PlayState <> GAME_SERVER Then
		WriteLine Server,tempstr	
		WriteByte(Server,ID)
		SendUDPMsg(Server,Players(0)\IP,Players(0)\Port)
	EndIf
End Function

Function SendToPlayers(tempid)
	;WriteInt(Server,tempid)
	
	For i = 1 To 31
		If Players(i) <> Null Then
			WriteInt(Server,tempid)
			SendUDPMsg(Server,Players(i)\IP,Players(i)\Port)
		EndIf
	Next
End Function

Function Button%(x,y,width,height,txt$, disabled%=False)
	Local Pushed = False
	
	Local ClrR = ColorRed()
	Local ClrG = ColorGreen()
	Local ClrB = ColorBlue()
	
	If Not disabled Then 
		If MouseX() > x And MouseX() < x+width Then
			If MouseY() > y And MouseY() < y+height Then
				If MouseDown(1) Then
					Pushed = True
					Color ClrR*0.6, ClrG*0.6, ClrB*0.6
				Else
					Color min(ClrR*1.4,255),min(ClrG*1.4,255),min(ClrB*1.4,255)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If Pushed Then 
		Rect x,y,width,height
		Color 133,130,125
		Rect x+1,y+1,width-1,height-1,False	
		Color 10,10,10
		Rect x,y,width,height,False
		Color 250,250,250
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1
	Else
		Rect x,y,width,height
		Color 133,130,125
		Rect x,y,width-1,height-1,False	
		Color 250,250,250
		Rect x,y,width,height,False
		Color 10,10,10
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1		
	EndIf
	
	Color 255,255,255
	If disabled Then Color 70,70,70
	Text x+width/2, y+height/2-1, txt, True, True
	
	Color 0,0,0
	
	If Pushed And MouseHit1 Then 
		PlaySound ButtonSFX 
		Return True
	EndIf
End Function

Function RectBox(x,y,width,height)
	Local ClrR = ColorRed()
	Local ClrG = ColorGreen()
	Local ClrB = ColorBlue()

	Rect x,y,width,height
	Color 133,130,125
	Rect x,y,width-1,height-1,False	
	Color 250,250,250
	Rect x,y,width,height,False
	Color 10,10,10
	Line x,y+height-1,x+width-1,y+height-1
	Line x+width-1,y,x+width-1,y+height-1		
	
	Color 0,0,0
End Function

Function UpdateServer()
	If Players(0)\IP = 1 Then
		PlayState=GAME_SERVER ;let the game know you're a server
		Server% = CreateUDPStream(8730) ;create a UDP stream with a specific port
		Players(0)=New Player
		Players(0)\IP=0 ;there is no IP for this "client"
		Players(0)\Port=0 ;there is no port for this "client"
		Players(0)\Pivot=collider ;player 1 is yourself
		Players(0)\Name = PlayerName ;this is the host's name
		Players(0)\LastMsgTime = MilliSecs() ;this sets a timer to detect if the connection with the server is still established
		Players(0)\Connected = True
		Players(0)\Rights = RIGHTS_HOST
		Players(0)\Act = ACT_FLASH
		Players(0)\VisibilityTimer = 0
		Players(0)\SpookCount = 3
		Players(0)\Red = 255
		Players(0)\Green = 255
		Players(0)\Blue = 255
		EntityType collider, hit_friendly
		PlayerCount=PlayerCount+1
		PlayingAs = PLAYER_CLASSD
		Rights = RIGHTS_HOST
		SpookCount = 3
		VisibilityTimer = 0
		SpookCooldown = 90
		SpookTimer = 0
		UpdateLights()
	EndIf
	
	Local getconn%
	MouseHit1 = MouseHit(1)
	MouseHit2 = MouseHit(2)
	
	If KeyHit(57) And PlayingAs <> PLAYER_CLASSD Then
		isFlying = Not isFlying
		SendToServer("fly")
		If isFlying Then
			EntityType collider,hit_map
		Else
			EntityType collider,hit_invisible
		EndIf
	EndIf
	
	Local isForw%,posx#,posy#,posz#,pyaw#,isFloor% ;isBack%,isLeft%,isRight%,
	If PlayState<>GAME_SOLO Then
		If (MilliSecs()>=UDPSend) Then
			UDPSend=MilliSecs()+UDP_FREQUENCY
		EndIf
	EndIf
	
	;Scare
	
	If MouseHit2 And SpookCount > 0 And VisibilityTimer < 0 And PlayingAs <> PLAYER_CLASSD Then
		AddChatMsg("You will be able to scare in " + ((SCARE_COOLDOWN+VisibilityTimer)/60)+ " seconds.",150,50,50,ID,False)
		MouseHit2 = False
	EndIf

	If MouseHit2 And SpookCount <= 0 And VisibilityTimer < 0 And PlayingAs <> PLAYER_CLASSD Then
		For i=0 To 31
			If Players(i)<>Null And i<>ID Then
				If Players(i)\KillTimer<=0 And Players(i)\PlayingAs = PLAYER_CLASSD Then
					Local dist# = Distance2(EntityX(Players(i)\Pivot), EntityY(Players(i)\Pivot), EntityZ(Players(i)\Pivot))
					
					If (Act = ACT_RUN And dist < 3.0) Or (Act = ACT_WALK And dist < 2.0) Or (Act = ACT_173 And dist < 4.0) Or (Act = ACT_BEHIND And dist < 2.0) Or (Act = ACT_DARKNESS And dist < 3.0) Then
						AddChatMsg("You are too close to a Class D.",150,50,50,ID,False)
						MouseHit2 = False
						i = 32
					EndIf
					
					If (Act = ACT_BEHIND And dist > 2.0) Then
						For j=(PlayState=GAME_SERVER) To 31
						If Players(j)<>Null And j <> ID Then
							If Players(j)\PlayingAs=PLAYER_CLASSD And Players(j)\Connected Then
								vXa# = EntityX(collider)-EntityX(Players(j)\Pivot)
								vZa# = EntityZ(collider)-EntityZ(Players(j)\Pivot)
								vLena# = Sqr((vXa*vXa)+(vZa*vZa))
								vYawa# = VectorYaw(vXa/vLena,0.0,vZa/vLena)-EntityYaw(Players(j)\Pivot)
								While vYawa<-180.0
									vYawa=vYawa+360.0
								Wend
								While vYawa>=180.0
									vYawa=vYawa-360.0
								Wend
								currfloor = (-EntityY(Players(j)\Pivot)-0.3)/2+0.1
								If Abs(PlayerFloor-currfloor) <= 1 And Abs(vYawa)<45 And EntityVisible(collider,Players(j)\Pivot) Then
									AddChatMsg("You can't appear when someone is looking at you.",150,50,50,ID,False)
									j = 32
									i = 32
									MouseHit2 = False
								EndIf
							EndIf
						EndIf
		Next
					EndIf
				EndIf
			EndIf
		Next
	EndIf

	; _______
	
	If PlayingAs <> PLAYER_CLASSD Then
		Stamina = STAMINA_MAX
	EndIf
	
	If Act = ACT_FLASH And VisibilityTimer > 0 Then
		CanMove = False
	EndIf

	If PlayState=GAME_SERVER Then
		Players(0)\PlayingAs = PlayingAs
		
		Select Players(0)\PlayingAs
			Case PLAYER_CLASSD
				Players(0)\Red = 255
				Players(0)\Green = 255
				Players(0)\Blue = 0
			Case PLAYER_MENTAL
				Players(0)\Red = 255
				Players(0)\Green = 100
				Players(0)\Blue = 100
			Case PLAYER_REDMIST
				Players(0)\Red = 255
				Players(0)\Green = 100
				Players(0)\Blue = 100
			Case PLAYER_EYEKILLER
				Players(0)\Red = 255
				Players(0)\Green = 100
				Players(0)\Blue = 100
		End Select
	
		Local messagesToSend%
		
		getconn = RecvUDPMsg(Server) ;AcceptTCPStream(Server)
		While getconn ;The server has received a message from a client
			messagesToSend = 0
			TempRead$ = ReadLine(Server)
			PlayerID% = ReadByte(Server)
			TempID%=Int(TempRead)
			
			;Receive act change by a monster
			If Mid(TempRead,1,3)="act" Then
				temp = Mid(TempRead,4,2)
				Players(PlayerID)\Act = temp
				
				If temp = 0 Then
					Players(PlayerID)\PlayingAs = PLAYER_CLASSD
					EntityTexture GetChild(Players(PlayerID)\Pivot,1),LoadTexture("GFX\npc\player.jpg")
					EntityType Players(PlayerID)\Pivot,hit_friendly
				ElseIf temp = ACT_FLASH Or temp = ACT_WALK Or temp = ACT_RUN Or temp = ACT_DARKNESS Then
					Players(PlayerID)\PlayingAs = PLAYER_MENTAL
					EntityTexture GetChild(Players(PlayerID)\Pivot,1),mental3
					EntityType Players(PlayerID)\Pivot,hit_invisible
				ElseIf temp = ACT_173 Or temp = ACT_FLASH_2 Or temp = ACT_BEHIND
					EntityTexture GetChild(Players(PlayerID)\Pivot,1),tex1732
					EntityType Players(PlayerID)\Pivot,hit_invisible
					Players(PlayerID)\PlayingAs = PLAYER_REDMIST
				ElseIf temp = ACT_BLUR Then
					EntityTexture GetChild(Players(PlayerID)\Pivot,1),eyekiller
					EntityType Players(PlayerID)\Pivot,hit_invisible
					Players(PlayerID)\PlayingAs = PLAYER_EYEKILLER
				EndIf
			EndIf
			
			;Receive rights change by an admin
			If Mid(TempRead,1,12)="rights_admin" Then
				temp = Mid(TempRead,13,2)
				Players(temp)\Rights = RIGHTS_ADMIN
			EndIf
			If Mid(TempRead,1,14)="rights_monster" Then
				temp = Mid(TempRead,15,2)
				Players(temp)\Rights = RIGHTS_MONSTER
			EndIf
			If Mid(TempRead,1,13)="rights_classd" Then
				temp = Mid(TempRead,14,2)
				Players(temp)\Rights = RIGHTS_CLASSD
				Players(temp)\PlayingAs = PLAYER_CLASSD
				EntityTexture GetChild(Players(PlayerID)\Pivot,1),LoadTexture("GFX\npc\player.jpg")
				EntityType Players(PlayerID)\Pivot,hit_friendly
			EndIf
			If Mid(TempRead,1,4)="kick" Then
				temp = Mid(TempRead,5,2)
				WriteInt Server,SERVER_MESSAGE_KICK ;kick the player
				SendUDPMsg Server,Players(temp)\IP,Players(temp)\Port ;tell the client to disconnect
				
				FreeEntity GetChild(Players(temp)\Pivot,1)
				FreeEntity Players(temp)\Pivot
				PlayerCount=PlayerCount-1
				AddChatMsg(Players(temp)\Name+ " has been kicked by " + Players(PlayerID)\Name+ ".",255,0,0,PlayerID,True)
				Players(temp)=Null
				Delete Players(temp)
			EndIf
			If Mid(TempRead,1,8)="teleport" Then
				temp = Mid(Tempread,9,2)
				
				PositionEntity(Players(PlayerID)\Pivot, EntityX(Players(temp)\Pivot),EntityY(Players(temp)\Pivot),EntityZ(Players(temp)\Pivot))
				ResetEntity Players(PlayerID)\Pivot
			EndIf
			If Mid(TempRead,1,4)="chat" Then
				AddChatMsg(Players(PlayerID)\Name + ": " + Mid(tempread, 5, Len(tempread)-4),Players(PlayerID)\Red,Players(PlayerID)\Green,Players(PlayerID)\Blue,PlayerID,True)
			EndIf
			If TempRead = "fly" Then
				Players(PlayerID)\IsFlying = Not Players(PlayerID)\IsFlying
			EndIf
			If Mid(TempRead,1,3) = "map" Then
				If Mid(TempRead,4,Len(TempRead)-3) = MapString Then 
					WriteLine(Server, "1")
				Else
					WriteLine(Server, MapString)
				EndIf
				SendUDPMsg Server,getconn,UDPMsgPort(Server)			
			EndIf
			
			If TempRead=("connected" + GAME_VERSION) Then ;the player connected and started generating the map
				For giveID=1 To 32
					If Players(giveID)<>Null Then
						If (Players(giveID)\IP = getconn) And (Players(giveID)\Port = UDPMsgPort(Server)) Then
							If Players(giveID)\Connected Then giveID = -1
							Exit
						EndIf
					EndIf
				Next
				If giveID>-1 Then
					For giveID=1 To 32
						If giveID >=32 Then giveID = -1 : Exit
						If (Players(giveID)=Null) Then Exit
					Next
				EndIf
				
				If Players(giveID)=Null Then
						Local PlayerTex = LoadTexture("GFX\npc\player.jpg")
						Players(giveID)=New Player
						Players(giveID)\IP=getconn
						Players(giveID)\Port=UDPMsgPort(Server)
						Players(giveID)\Pivot=CreatePivot()
						Players(giveID)\Stamina=STAMINA_MAX
						Players(giveID)\Rights=RIGHTS_CLASSD
						Players(giveID)\Act = ACT_FLASH
						Players(giveID)\VisibilityTimer=0
						Players(giveID)\SpookCount = 3
						Players(giveID)\isFlying = False
						Players(giveID)\Red = 255
						Players(giveID)\Green = 255
						Players(giveID)\Blue = 0
						Local tempMesh = LoadAnimMesh("GFX\npc\mental.b3d")
						ScaleEntity tempMesh, 0.15,0.15,0.15
						PositionEntity tempMesh,0.0,ColliderDefaultY#,0.0,True
						EntityParent tempMesh,Players(giveID)\Pivot
						EntityTexture tempMesh,PlayerTex
						PositionEntity Players(giveID)\Pivot,-2.5, ColliderDefaultY#, -0.5,True
						EntityRadius Players(giveID)\Pivot, ColliderXRadius#,ColliderYRadius#
						EntityType Players(giveID)\Pivot,hit_friendly
						Players(giveID)\PlayingAs = PLAYER_CLASSD
						
						Select Players(giveID)\PlayingAs
							Case PLAYER_CLASSD
								EntityTexture GetChild(Players(giveID)\Pivot,1),PlayerTex
								EntityType Players(giveID)\Pivot, hit_friendly
							Case PLAYER_MENTAL
								EntityTexture GetChild(Players(giveID)\Pivot,1),mental
								EntityType Players(giveID)\Pivot, hit_invisible;monster
							Case PLAYER_REDMIST
								EntityTexture GetChild(Players(giveID)\Pivot,1),tex173
								EntityType Players(giveID)\Pivot, hit_invisible;monster
							Case PLAYER_EYEKILLER
								EntityTexture GetChild(Players(giveID)\Pivot,1),eyekiller
								EntityType Players(giveID)\Pivot, hit_invisible;monster
						End Select

					Players(giveID)\LastMsgTime=MilliSecs()
					Players(giveID)\Name = ReadLine(Server)
					WriteLine(Server,MapString) ;give the player the server's map arrangement
					WriteByte(Server,giveID) ;give the player their ID
					WriteInt(Server, SCARE_COOLDOWN)
					SendUDPMsg Server,getconn,UDPMsgPort(Server) ;give the message to the player
					PlayerCount=PlayerCount+1
				Else ;server is full
					WriteLine Server,"KICK" ;kick the player
					SendUDPMsg Server,getconn,UDPMsgPort(Server) ;tell the client to disconnect
				EndIf
			ElseIf Mid(TempRead,1,9)="connected" Then ;client has got another version of the game
				WriteLine(Server,"version" +GAME_VERSION)
				WriteByte(Server,giveID)
				SendUDPMsg(Server,getconn,UDPMsgPort(Server))
			Else If TempRead="disconnect" Then ;player has "cleanly" disconnected
				TempID%=PlayerID
				If Players(TempID)<>Null Then ;this player exists: remove it
					If Players(TempID)\IP = getconn And Players(TempID)\Port = UDPMsgPort(Server) Then
						FreeEntity GetChild(Players(TempID)\Pivot,1)
						FreeEntity Players(TempID)\Pivot
						PlayerCount=PlayerCount-1
						AddChatMsg(Players(TempID)\Name+ " has disconnected.",255,255,0,TempID,True)
						Players(TempID)=Null
						Delete Players(TempID)
					EndIf
				EndIf
			Else
				If Players(TempID)<>Null Then ;player exists
					If Players(TempID)\IP = getconn Then
						If Players(TempID)\Connected = False Then
							AddChatMsg(Players(TempID)\Name+ " has joined the server.",255,255,0,TempID,True)
						EndIf
						Players(TempID)\Connected = True
						Local newMsg%=ReadInt(Server)
						If Players(TempID)\LastClientMsg<newMsg Then
							Players(TempID)\LastMsgTime=MilliSecs()
							
							For j%=0 To 59
								Players(TempID)\DirList[j] = ReadByte(Server)
								Players(TempID)\DirListAngle[j] = ReadFloat(Server)
							Next
							Players(TempID)\DirsToRead=max(Players(TempID)\DirsToRead+(newMsg-Players(TempID)\LastClientMsg),0)
							;pyaw# = ReadFloat(Server)
							;RotateEntity Players(TempID)\Pivot,0.0,pyaw,0.0,True
							Players(TempID)\LastClientMsg=newMsg
							Local chatID% = ReadInt(Server)
							Local chatMsg$ = ReadLine(Server)
							If chatID>Players(TempID)\LastChatMsgID Then
								Players(TempID)\LastChatMsgID = chatID
								If Len(chatMsg)>0 Then
									If Players(TempID)\PlayingAs=PLAYER_CLASSD Then
										AddChatMsg(Players(TempID)\Name+ ": " +chatMsg,50,70,255,TempID,False)
									Else
										AddChatMsg(Players(TempID)\Name+ ": " +chatMsg,150,0,0,TempID,True)
									EndIf
								EndIf
							EndIf
							Players(TempID)\LastRecvChatMsg = ReadInt(Server)
						EndIf
					EndIf
				EndIf
			EndIf
			getconn = RecvUDPMsg(Server)
		Wend
		
		SpookCooldown=max(SpookCooldown-1,0)
		VisibilityTimer=max(VisibilityTimer-1,-10000)
		
		For i=1 To 31
			isFloor=False
			
			If (Players(i)<>Null) Then
					If Players(i)\Connected Then
						Select Players(i)\PlayingAs
							Case PLAYER_CLASSD
								Players(i)\Red = 255
								Players(i)\Green = 255
								Players(i)\Blue = 0
							Case PLAYER_MENTAL
								Players(i)\Red = 255
								Players(i)\Green = 100
								Players(i)\Blue = 100
							Case PLAYER_REDMIST
								Players(i)\Red = 255
								Players(i)\Green = 100
								Players(i)\Blue = 100
							Case PLAYER_EYEKILLER
								Players(i)\Red = 255
								Players(i)\Green = 100
								Players(i)\Blue = 100
						End Select
						
						WriteInt(Server,SentMsgID)
						For j=0 To 31
							If (Players(j)<>Null) Then
								WriteByte(Server,j+1)
								WriteInt(Server,Players(j)\LastClientMsg)
								If Players(j)\Pivot<>collider Then
									WriteByte(Server,(Players(j)\MForw)+(Players(j)\MBack*2)+(Players(j)\MLeft*4)+(Players(j)\MRight*8)+(Players(j)\MShift*16))
									WriteByte(Server,Players(j)\PlayingAs)
									WriteByte(Server,Players(j)\Rights)
									WriteByte(Server,(Players(j)\KillTimer)>0)
									WriteInt(Server,Players(j)\VisibilityTimer)
									WriteInt(Server,Players(j)\SpookCount)
									WriteByte(Server,Players(j)\Act)
									WriteLine(Server,Players(j)\Name)
									WriteLine(Server,Players(j)\IsFlying)
									
									WriteFloat(Server,EntityX(Players(j)\Pivot,True))
									WriteFloat(Server,EntityY(Players(j)\Pivot,True))
									WriteFloat(Server,EntityZ(Players(j)\Pivot,True))
									WriteFloat(Server,EntityYaw(Players(j)\Pivot,True))
								Else
									WriteByte(Server,MoveForw+(MoveBack*2)+(MoveLeft*4)+(MoveRight*8)+(MoveShift*16))
									WriteByte(Server,PlayingAs)
									WriteByte(Server,Rights)
									WriteByte(Server,KillTimer>0)
									WriteInt(Server,VisibilityTimer)
									WriteInt(Server,SpookCount)
									WriteByte(Server,Act)
									WriteLine(Server,PlayerName)
									WriteLine(Server,IsFlying)
									
									WriteFloat(Server,EntityX(collider,True))
									WriteFloat(Server,EntityY(collider,True))
									WriteFloat(Server,EntityZ(collider,True))
									WriteFloat(Server,EntityYaw(collider,True))
								EndIf
								
								;WriteLine(Server,Players(j)\TexturePath)
							Else
								WriteByte(Server,0)
							EndIf
						Next
						
						WriteInt(Server,Players(i)\LastSpookSent)
						
						Local cm.ChatMessage
						For cm = Each ChatMessage
							If cm\ID>Players(i)\LastRecvChatMsg And cm\SendTo[i] Then messagesToSend=messagesToSend+1
						Next
						WriteByte(Server,messagesToSend)
						For cm = Each ChatMessage
							If cm\ID>Players(i)\LastRecvChatMsg And cm\SendTo[i] Then
								WriteInt(Server,cm\ID)
								WriteLine(Server,cm\Txt)
								WriteByte(Server,cm\R)
								WriteByte(Server,cm\G)
								WriteByte(Server,cm\B)
							EndIf
						Next
						
						SendUDPMsg Server,Players(i)\IP,Players(i)\Port
					EndIf
					
					pyaw = EntityYaw(Players(i)\Pivot,True)
					If Players(i)\KillTimer<=0 Then
						Local animmed% = Players(i)\DirList[0]<>0
						Players(i)\DirsToRead = min(59,Players(i)\DirsToRead)
						While Players(i)\DirsToRead>0
							isForw = Players(i)\DirList[Players(i)\DirsToRead]
							Players(i)\DirsToRead=Players(i)\DirsToRead-1
							pmoves = False
							If isForw>=64 Then
								isForw=isForw-64 : Players(i)\MShift = True
							Else
								Players(i)\MShift = False
							EndIf
							If isForw>=32 Then
								isForw=isForw-32 : Players(i)\MClick2 = True
							Else
								Players(i)\MClick2 = False
							EndIf
							If isForw>=16 Then
								isForw=isForw-16 : Players(i)\MClick1 = True
							Else
								Players(i)\MClick1 = False
							EndIf
							If isForw>=8 Then
								isForw=isForw-8 : Players(i)\MRight = True : pmoves = True
							Else
								Players(i)\MRight = False
							EndIf
							If isForw>=4 Then
								isForw=isForw-4 : Players(i)\MLeft = True : pmoves = True
							Else
								Players(i)\MLeft = False
							EndIf
							If isForw>=2 Then
								isForw=isForw-2 : Players(i)\MBack = True : pmoves = True
							Else
								Players(i)\MBack = False
							EndIf
							If isForw>=1 Then
								isForw=isForw-1 : Players(i)\MForw = True : pmoves = True
							Else
								Players(i)\MForw = False
							EndIf
							
							Local speedMult# = 1.0
							If Players(i)\PlayingAs<>PLAYER_CLASSD And Players(i)\VisibilityTimer<0 Then
								speedMult = 2.0
							EndIf
							
							If pmoves And Players(i)\MShift Then
								
								If Players(i)\Stamina > 0 And (Players(i)\PlayingAs=PLAYER_CLASSD Or Players(i)\Act = ACT_RUN) Then
									speedMult = 1.7
								EndIf
								
								If (Players(i)\Act = ACT_173 Or Players(i)\Act = ACT_FLASH_2 Or Players(i)\Act = ACT_BEHIND) And Players(i)\VisibilityTimer > 0 Then
									speedMult = 3.0
								EndIf
								
								If Players(i)\PlayingAs = PLAYER_CLASSD Then Players(i)\Stamina = Players(i)\Stamina - 1
							EndIf
							
							If Players(i)\VisibilityTimer > 0 And Players(i)\Act = ACT_DARKNESS Then
								speedMult = 0.5
							EndIf
							
							If Players(i)\VisibilityTimer+1>0 And Players(i)\VisibilityTimer<=0 Then
								EntityType Players(i)\Pivot,hit_invisible
							EndIf
							
							If Players(i)\PlayingAs<>PLAYER_CLASSD Then
								If Players(i)\MClick1 Then
									DebugLog "client lclick"
									If Players(i)\VisibilityTimer>0 And Players(i)\Act <> ACT_FLASH And Players(i)\Act <> ACT_FLASH_2 And Players(i)\Act <> ACT_BLUR Then
										For j=1 To 31
											If Players(j)<>Null Then
												If Players(j)\PlayingAs=PLAYER_CLASSD And i<>j Then
													vX# = EntityX(Players(j)\Pivot)-EntityX(Players(i)\Pivot)
													vZ# = EntityZ(Players(j)\Pivot)-EntityZ(Players(i)\Pivot)
													vLen# = Sqr((vX*vX)+(vZ*vZ))
													vYawa# = VectorYaw(vX/vLen,0.0,vZ/vLen)-Players(i)\DirListAngle[Players(i)\DirsToRead]
													While vYawa<-180.0
														vYaw=vYaw+360.0
													Wend
													While vYawa>=180.0
														vYawa=vYawa-360.0
													Wend
													;DebugLog "ANGLE " +Abs(vYaw)
													If EntityDistance(Players(j)\Pivot,Players(i)\Pivot)<0.8 And Abs(vYawa)<45 Then
														If Players(j)\KillTimer<=0 Then AddChatMsg(Players(i)\Name+ " killed " +Players(j)\Name+ "!",255,100,0,i,True)
														Players(j)\KillTimer = max(Players(j)\KillTimer,1)
													EndIf
												EndIf
											EndIf
										Next
										If PlayingAs=PLAYER_CLASSD Then
											Local vXb# = EntityX(collider)-EntityX(Players(i)\Pivot)
											Local vZb# = EntityZ(collider)-EntityZ(Players(i)\Pivot)
											Local vLenb# = Sqr((vXb*vXb)+(vZb*vZb))
											Local vYawb# = VectorYaw(vXb/vLenb,0.0,vZb/vLenb)-Players(i)\DirListAngle[Players(i)\DirsToRead]
											While vYawb<-180.0
												vYawb=vYawb+360.0
											Wend
											While vYaw>=180.0
												vYawb=vYawb-360.0
											Wend
											If EntityDistance(collider,Players(i)\Pivot)<0.8 And Abs(vYawb)<45 And KillTimer<=0 Then
												KillTimer = max(KillTimer,1)
												AddChatMsg(Players(i)\Name+ " killed " +PlayerName+ "!",255,100,0,i,True)
											EndIf
										EndIf
									EndIf
								EndIf
								
								If Players(i)\IsFlying Then
									EntityType Players(i)\Pivot,hit_map
									Players(i)\fallspeed = 0.0
								Else
									EntityType Players(i)\Pivot,hit_invisible
								EndIf
								
								If Players(i)\MClick2 Then
									DebugLog "client rclick"
									If Players(i)\SpookCount<3 And Players(i)\SpookCooldown<=0 Then
										Players(i)\SpookCount=Players(i)\SpookCount+1
										;Players(i)\SpookTimer=40
										Players(i)\SpookCooldown=95
										;If Players(i)\VisibilityTimer<0 Then
										;	Players(i)\VisibilityTimer = 150
										;	EntityType Players(i)\Pivot,hit_monster
										;Else
										;	Players(i)\VisibilityTimer=Players(i)\VisibilityTimer+150
										;EndIf
										
										EntityType Players(i)\Pivot,hit_monster
										
										If Players(i)\Act = ACT_FLASH Then
											Players(i)\VisibilityTimer = 30+1
											Players(i)\SpookCount=Players(i)\SpookCount+1
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_WALK Then
											Players(i)\VisibilityTimer = 300+1
											Players(i)\SpookCount=3
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_RUN Then
											Players(i)\VisibilityTimer = 360+1
											Players(i)\SpookCount=3
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_173 Then
											Players(i)\VisibilityTimer = 1200+1
											Players(i)\SpookCount=3
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_FLASH_2 Then
											Players(i)\VisibilityTimer = 60+1
											Players(i)\SpookCount=Players(i)\SpookCount+1
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_BEHIND Then
											Players(i)\VisibilityTimer = 300+1
											Players(i)\SpookCount=3
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_BLUR Then
											Players(i)\VisibilityTimer = 1200+1
											Players(i)\SpookCount=3
											Players(i)\SpookCooldown=95
										EndIf
										
										If Players(i)\Act = ACT_DARKNESS Then
											Players(i)\VisibilityTimer = 300+1
											Players(i)\SpookCount=3
											Players(i)\SpookCooldown=95
										EndIf

										
										LastSentSpook=LastSentSpook+1
										For j=1 To 31
											If Players(j)<>Null Then
												If EntityDistance(Players(j)\Pivot,Players(i)\Pivot)<8.0 Then 
													Players(j)\LastSpookSent = LastSentSpook
												EndIf
											EndIf
										Next
										If EntityDistance(collider,Players(i)\Pivot)<8.0 Then 
											SpookTimer = 40
										EndIf
									EndIf
								EndIf
							EndIf
							
							If Players(i)\MForw Then MoveEntity Players(i)\Pivot,0,0,0.02*speedMult
							If Players(i)\MBack Then MoveEntity Players(i)\Pivot,0,0,-0.016*speedMult
							If Players(i)\MLeft Then MoveEntity Players(i)\Pivot,-0.012*speedMult, 0, 0
							If Players(i)\MRight Then MoveEntity Players(i)\Pivot,0.012*speedMult,0,0
							Players(i)\Stamina% = min(STAMINA_MAX,Players(i)\Stamina%+0.25)
							RotateEntity Players(i)\Pivot,0.0,Players(i)\DirListAngle[Players(i)\DirsToRead],0.0,True
							
							If Players(i)\KillTimer=0 Then
								If Players(i)\MForw+Players(i)\MBack+Players(i)\MLeft+Players(i)\MRight>0 Then
								
									SetAnimTime GetChild(Players(i)\Pivot,1),AnimTime(GetChild(Players(i)\Pivot,1))+0.2*speedMult
									If AnimTime(GetChild(Players(i)\Pivot,1)) >= 14.0 Then
										SetAnimTime GetChild(Players(i)\Pivot,1),1
									EndIf
									
									;Move Forward Animation
									
									;If Players(i)\MForw > 0 Then
									;	SetAnimTime GetChild(Players(i)\Pivot,1),AnimTime(GetChild(Players(i)\Pivot,1))+0.2*speedMult
									;	If AnimTime(GetChild(Players(i)\Pivot,1)) >= 14.0 Then
									;		SetAnimTime GetChild(Players(i)\Pivot,1),0
									;	EndIf
									;EndIf
									
									;Move Backward Animation
									
									;If Players(i)\MBack > 0 Then
									;	SetAnimTime GetChild(Players(i)\Pivot,1),AnimTime(GetChild(Players(i)\Pivot,1))-0.2*speedMult
									;	If AnimTime(GetChild(Players(i)\Pivot,1)) <= 1 Then
									;		SetAnimTime GetChild(Players(i)\Pivot,1),13.0
									;	EndIf
									;EndIf
									
									animmed = True
								Else
									If (Players(i)\Act = ACT_173 Or Players(i)\Act = ACT_BLUR) And Players(i)\VisibilityTimer > 0 Then
										Animate2(GetChild(Players(i)\Pivot,1), AnimTime(GetChild(Players(i)\Pivot,1)), 205, 299, 0.08)
										animmed = True
									Else
										SetAnimTime GetChild(Players(i)\Pivot,1),AnimStandTime
									EndIf
								EndIf
							EndIf
							
							If Not Players(i)\isFlying Then Players(i)\FallSpeed = Players(i)\FallSpeed-0.004
								
							If Players(i)\FallSpeed < -0.18 And EntityY(Players(i)\Pivot,True)<-1 And Players(i)\PlayingAs = PLAYER_CLASSD Then
								Players(i)\KillTimer=max(1,Players(i)\KillTimer)
								AddChatMsg(Players(i)\Name+ " fell to their death!",255,0,0,i,True)
								Players(i)\FallSpeed = 0
							EndIf
							
							MoveEntity Players(i)\Pivot,0,Players(i)\FallSpeed,0	
							
							UpdateWorld ;the server needs to be good enough to run this multiple times
							
							isFloor = False
							For j=1 To CountCollisions(Players(i)\Pivot)
								If CollisionY(Players(i)\Pivot,j) < EntityY(Players(i)\Pivot) - 0.1 Then isFloor = True : Exit
							Next
							
							If isFloor = True Then
								If Players(i)\FallSpeed < -0.09 And EntityY(Players(i)\Pivot)<-1 And Players(i)\PlayingAs = PLAYER_CLASSD Then
									Players(i)\KillTimer=max(1,Players(i)\KillTimer)
									AddChatMsg(Players(i)\Name+ " fell to their death!",255,0,0,i,True)
								EndIf
								Players(i)\FallSpeed = 0
							Else
								
							EndIf
						Wend
						
							
						If Not animmed Then
							;SetAnimTime GetChild(Players(i)\Pivot,1),AnimStandTime 
						EndIf
						
						;Acts
								dist# = Distance2(EntityX(Players(i)\Pivot),EntityY(Players(i)\Pivot),EntityZ(Players(i)\Pivot))
								If Players(i)\Act = ACT_FLASH And Players(i)\VisibilityTimer > 0 And dist < 4.0 Then
									If Players(i)\VisibilityTimer = 30 Then PlaySound(HorrorSFX(Rand(0, 9)))
									AmbientLight 255,255,255
									If Players(i)\VisibilityTimer = 1 Then UpdateLights()
								EndIf
								
								If Players(i)\Act = ACT_FLASH_2 And Players(i)\VisibilityTimer > 0 And dist < 8.0 Then
									If Players(i)\VisibilityTimer = 60 Then PlaySound(HorrorSFX(Rand(0, 9)))
									EntityFX GetChild(Players(i)\Pivot,1),8
									If Players(i)\VisibilityTimer = 1 Then  EntityFX GetChild(Players(i)\Pivot,1),0
								EndIf
								
								If Players(i)\Act = ACT_WALK And Players(i)\VisibilityTimer > 0 And dist < 8.0 Then
									If Players(i)\VisibilityTimer = 300 Then PlaySound(HorrorSFX(Rand(0, 9)))
									AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
									
									If Players(i)\VisibilityTimer = 1 Then UpdateLights()
								EndIf
								
								If Players(i)\Act = ACT_RUN And Players(i)\VisibilityTimer > 0 And dist < 8.0 Then
									Local temptimer = Players(i)\VisibilityTimer
									
									If temptimer  = 360 Or temptimer = 240 Or temptimer = 120 Then
										If temptimer = 360 Then PlaySound(HorrorSFX(Rand(0, 9)))
										CameraFogRange camera, 1, 5.4
										AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
									EndIf
									
									If temptimer = 300 Or temptimer = 170 Or temptimer = 50 Then
										If temptimer = 300 Then PlaySound(HorrorSFX(Rand(0, 9)))
										CameraFogRange camera, 1, 10
										AmbientLight 255,255,255
									EndIf
									
									If temptimer = 1 Then
										UpdateLights()
									EndIf
								EndIf
								
								If Players(i)\Act = ACT_173 And Players(i)\VisibilityTimer > 0 And dist < 8.0 Then
									If ChannelPlaying(SoundChannel) = False And Players(i)\VisibilityTimer > 120 Then SoundChannel = PlaySound(DontlookSFX)
									EntityFX GetChild(Players(i)\Pivot,1),8
									If Players(i)\VisibilityTimer = 1 Then  EntityFX GetChild(Players(i)\Pivot,1),0
								EndIf
								
								If Players(i)\Act = ACT_BEHIND And Players(i)\VisibilityTimer > 0 And dist < 8.0 Then
									If Players(i)\VisibilityTimer = 300 Then PlaySound(BehindSFX)
									EntityFX GetChild(Players(i)\Pivot,1),8
									If Players(i)\VisibilityTimer = 1 Then  EntityFX GetChild(Players(i)\Pivot,1),0
								EndIf
								
								If Players(i)\Act = ACT_BLUR And Players(i)\VisibilityTimer > 0 And dist < 8.0 Then
									If ChannelPlaying(SoundChannel) = False And Players(i)\VisibilityTimer > 600 Then SoundChannel = PlaySound(EyekillerSFX)
									EntityFX GetChild(Players(i)\Pivot,1),8
									If EntityVisible(Players(i)\Pivot, camera) Then BlurTimer = 270
								EndIf
								
								If Players(i)\Act = ACT_DARKNESS And Players(i)\VisibilityTimer > 0 And dist < 5.0 Then
									If Players(i)\VisibilityTimer = 300 Then PlaySound(FireOff) : BlurTimer = 270 : AmbientLight 10,10,10
									If Players(i)\VisibilityTimer = 50 Then PlaySound(FireOn)
									If Players(i)\VisibilityTimer = 1 Then UpdateLights()
								EndIf
						
					EndIf
					
					If Players(i)\KillTimer>0 Then
						Players(i)\DirsToRead = 0
						Players(i)\FallSpeed = 0.0
						Players(i)\SpookCooldown = 10.0
						Players(i)\KillTimer=min(Players(i)\KillTimer+1,100)
						If Players(i)\KillTimer>95 Then
							Local FloorZ#,FloorY#,StartX#,EndX#
							Local thisPlayerFloor% = (-EntityY(Players(i)\Pivot)-0.5)/2
							thisPlayerFloor=max(thisPlayerFloor/2,1)
							FloorY#=-(thisPlayerFloor-1)*2-1.0
							If Floor(thisPlayerFloor/2.0)=Ceil(thisPlayerFloor/2.0) Then ;parillinen
								FloorZ#=-6.54
								StartX# = 7.2 
								EndX# = 0.8
							Else ;pariton
								FloorZ#=-0.31
								StartX# = 0.8
								EndX# = 7.2
							EndIf
							
							PositionEntity Players(i)\Pivot,Rnd(StartX,EndX),FloorY+0.5,FloorZ,True
							;ResetEntity Players(i)\Pivot
							If LinePick(EntityX(Players(i)\Pivot,True),EntityY(Players(i)\Pivot,True),EntityZ(Players(i)\Pivot,True),0.0,-40.0,0.0,0.0)<>0 Then
								PositionEntity Players(i)\Pivot,EntityX(Players(i)\Pivot,True),PickedY()+0.5,EntityZ(Players(i)\Pivot,True),True
								;ResetEntity Players(i)\Pivot
								DebugLog "PICKED!"
							EndIf
							UpdateWorld
							Players(i)\KillTimer = 0
						EndIf
						If AnimTime(GetChild(Players(i)\Pivot,1)) = 301.0 Then SetAnimTime GetChild(Players(i)\Pivot,1),166
						SetAnimTime GetChild(Players(i)\Pivot,1),min(max(AnimTime(GetChild(Players(i)\Pivot,1))+0.1,166),173)
					EndIf
					
					
					If Players(i)\VisibilityTimer<0 And Players(i)\SpookCount>0 Then Players(i)\SpookCount=3
					If Players(i)\VisibilityTimer<=-SCARE_COOLDOWN Then Players(i)\SpookCount=0
					Players(i)\SpookCooldown=max(Players(i)\SpookCooldown-1,0)
					Players(i)\VisibilityTimer=max(Players(i)\VisibilityTimer-1,-10000)
					
					PositionEntity GetChild(Players(i)\Pivot,1),EntityX(Players(i)\Pivot,True),EntityY(Players(i)\Pivot,True),EntityZ(Players(i)\Pivot,True),True
					ScaleEntity GetChild(Players(i)\Pivot,1),0.15,0.15,0.15,True
					TranslateEntity GetChild(Players(i)\Pivot,1),0.0,-0.7,0.0,True
					;RotateEntity Players(i)\Pivot,0.0,pyaw,0.0,True
					
					If (MilliSecs()-Players(i)\LastMsgTime>AUTO_DISCONNECT_TIME) Then ;remove client after X seconds of inactivity: assume connection was unexpectedly lost
						FreeEntity GetChild(Players(i)\Pivot,1)
						FreeEntity Players(i)\Pivot
						AddChatMsg(Players(i)\Name+ " timed out.",255,0,0,i,True)
						PlayerCount=PlayerCount-1
						Delete Players(i)
						Players(i) = Null
					Else
						PositionEntity GetChild(Players(i)\Pivot,1),EntityX(Players(i)\Pivot,True),EntityY(Players(i)\Pivot,True),EntityZ(Players(i)\Pivot,True),True
						ScaleEntity GetChild(Players(i)\Pivot,1),0.15,0.15,0.15,True
						TranslateEntity GetChild(Players(i)\Pivot,1),0.0,-0.7,0.0
					EndIf
				EndIf
				
		Next
		If PlayerCount>1 Then SentMsgID=SentMsgID+1
	
	Else If PlayState=GAME_CLIENT Then

		getconn = RecvUDPMsg(Server)
		VisibilityTimer = VisibilityTimer - 1

		While getconn ;the server has given you a message
			Local ttmp = ReadInt(Server)
			If ttmp = SERVER_MESSAGE_DISCONNECT Then
				RuntimeError("The host has closed the server")
			EndIf
			If ttmp = SERVER_MESSAGE_KICK Then
				RuntimeError("You have been kicked from the server")
			EndIf
			If ttmp>LastMsgID Then
				PlayerCount = 0
				Players(0)\LastMsgTime = MilliSecs()
				LastMsgID = ttmp
				For i=0 To 31
					ttmp% = ReadByte(Server)

					;DebugLog "ttmp " +(ttmp-1)
					If ttmp Then
						PlayerCount = PlayerCount + 1
						Players(ttmp-1)\Connected = True
						Local lastClientMsg% = ReadInt(Server)
						isForw = ReadByte(Server)
						Local character% = ReadByte(Server)
						Local prights% = ReadByte(Server)
						Local isDead% = ReadByte(Server)
						Local VisibleTimer% = ReadInt(Server)
						Local SpookCount2% = ReadInt(Server)
						Players(ttmp-1)\Act = ReadByte(Server)
						Players(ttmp-1)\Name = ReadLine(Server)
						IsFlying = ReadLine(Server)
						If Players(ttmp-1)<>Null Then
							If isForw>=16 Then
								isForw=isForw-16 : Players(ttmp-1)\MShift = True
							Else
								Players(ttmp-1)\MShift = False
							EndIf
							If isForw>=8 Then
								isForw=isForw-8 : Players(ttmp-1)\MRight = True
							Else
								Players(ttmp-1)\MRight = False
							EndIf
							If isForw>=4 Then
								isForw=isForw-4 : Players(ttmp-1)\MLeft = True
							Else
								Players(ttmp-1)\MLeft = False
							EndIf
							If isForw>=2 Then
								isForw=isForw-2 : Players(ttmp-1)\MBack = True
							Else
								Players(ttmp-1)\MBack = False
							EndIf
							If isForw>=1 Then
								isForw=isForw-1 : Players(ttmp-1)\MForw = True
							Else
								Players(ttmp-1)\MForw = False
							EndIf
						EndIf
						posx# = ReadFloat(Server)
						posy# = ReadFloat(Server)
						posz# = ReadFloat(Server)
						pyaw# = ReadFloat(Server)
						If ttmp-1<>ID Then
							;If posx<>EntityX(Players(ttmp-1)\Pivot,True) Or posz<>EntityZ(Players(ttmp-1)\Pivot,True) Then
							;	SetAnimTime GetChild(Players(ttmp-1)\Pivot,1),AnimTime(GetChild(Players(ttmp-1)\Pivot,1))+0.1
							;	If AnimTime(GetChild(Players(ttmp-1)\Pivot,1)) >= 14.0 Then
							;		SetAnimTime GetChild(Players(ttmp-1)\Pivot,1),0
							;	EndIf
							;Else
							;	SetAnimTime GetChild(Players(ttmp-1)\Pivot,1),15.0
							;EndIf
							If Players(ttmp-1)<>Null Then
								PositionEntity Players(ttmp-1)\Pivot,posx#,posy#,posz#,True
								PositionEntity GetChild(Players(ttmp-1)\Pivot,1),posx#,posy#-0.7,posz#,True
								RotateEntity Players(ttmp-1)\Pivot,0.0,pyaw,0.0,True
								ResetEntity Players(ttmp-1)\Pivot
								If Players(ttmp-1)\PlayingAs<>character Then
									Local PlayerTexture = LoadTexture("GFX\npc\player.jpg")
									Select character
										Case PLAYER_CLASSD
											EntityTexture GetChild(Players(ttmp-1)\Pivot,1),PlayerTexture
											EntityType Players(ttmp-1)\Pivot,hit_friendly
										Case PLAYER_MENTAL
											EntityTexture GetChild(Players(ttmp-1)\Pivot,1),mental
											EntityType Players(ttmp-1)\Pivot,hit_invisible
										Case PLAYER_REDMIST
											EntityTexture GetChild(Players(ttmp-1)\Pivot,1),tex173
											EntityType Players(ttmp-1)\Pivot,hit_invisible
										Case PLAYER_EYEKILLER
											EntityTexture GetChild(Players(ttmp-1)\Pivot,1),eyekiller
											EntityType Players(ttmp-1)\Pivot,hit_invisible
									End Select
								EndIf
								If character<>PLAYER_CLASSD Then
									If VisibleTimer<0 Then
										EntityAlpha GetChild(Players(ttmp-1)\Pivot,1),0.5
									Else
										EntityAlpha GetChild(Players(ttmp-1)\Pivot,1),1.0
									EndIf
								Else
									EntityAlpha GetChild(Players(ttmp-1)\Pivot,1),1.0
								EndIf

								Players(ttmp-1)\Rights = prights
								Players(ttmp-1)\PlayingAs = character
								Players(ttmp-1)\KillTimer = isDead
								If Players(ttmp-1)\VisibilityTimer > 0 Then
									Players(ttmp-1)\VisibilityTimer = Players(ttmp-1)\VisibilityTimer - 1
								Else
									Players(ttmp-1)\VisibilityTimer = VisibleTimer
								EndIf
								If Abs(Players(ttmp-1)\VisibilityTimer-VisibleTimer) > 20 Then Players(ttmp-1)\VisibilityTimer = VisibleTimer
								
							EndIf
						
						Else
							If PlayingAs<>PLAYER_CLASSD Then
								If VisibilityTimer > 0 Then
									;VisibilityTimer = VisibilityTimer - 1 Moved in another place
								Else
									VisibilityTimer=VisibleTimer
								EndIf
							Else
								VisibilityTimer=0
							EndIf
							If Abs(VisibilityTimer-VisibleTimer) > 20 Then VisibilityTimer = VisibleTimer
							
							If isDead Then
								Kill()
								PositionEntity collider,posx,posy,posz
							Else
								If KillTimer>0 Then PositionEntity collider,posx,posy,posz : UpdateLights()
								KillTimer=0
							EndIf
							
							Local prightsstring$
							If prights = 0 Then
								prightsstring = "Admin"
							Else If prights = 1 Then
								prightsstring = "Monster"
							Else
								prightsstring = "Class D"
							EndIf
							If prights <> Rights Then
								If Rights > prights Then
									AddChatMsg("You have been promoted to " + prightsstring + ".",0,255,0,ID,False)
								Else
									AddChatMsg("You have been demoted to " + prightsstring + ".",255,0,0,ID,False)
								EndIf
							EndIf
							If character = PLAYER_CLASSD And PlayingAs <> PLAYER_CLASSD Then
								AmbientLight Brightness,Brightness,Brightness
								CameraFogRange camera,1,2.8
							EndIf

							Rights = prights
							PlayingAs = character
							SpookCount = SpookCount2
							Players(ID)\Rights = prights
							Players(ID)\PlayingAs = character
							Players(ID)\KillTimer = isDead
							Players(ID)\VisibilityTimer = VisibilityTimer
							;ReadLine(Server)
							Local bbb% = DirListShift-(SentMsgID-lastClientMsg)
							If bbb<0 Then bbb=bbb+60
							If bbb>=0 And (SentMsgID-lastClientMsg)<60 And (SentMsgID-lastClientMsg)>0 Then
								If (Not FloatEquals(MemX[bbb],posx)) Or (Not FloatEquals(MemZ[bbb],posz)) Then
									PositionEntity collider,posx,posy,posz
									ResetEntity collider
									;DebugLog "Correction! " +(SentMsgID-lastClientMsg)
								EndIf
								Client_HighPacketLose = False
							Else
								Client_HighPacketLose = True
							EndIf
						EndIf
						
						If Players(ttmp-1)<>Null Then
							If EntityVisible(Players(ttmp-1)\Pivot,camera) And EntityInView(Players(ttmp-1)\Pivot, camera) Then
								Players(ttmp-1)\IsVisible = True
							Else
								Players(ttmp-1)\IsVisible = False
							EndIf
							;Acts
								
								dist# = Distance2(EntityX(Players(ttmp-1)\Pivot),EntityY(Players(ttmp-1)\Pivot),EntityZ(Players(ttmp-1)\Pivot))
						
								If Players(ttmp-1)\Act = ACT_FLASH And Players(ttmp-1)\VisibilityTimer > 0 And dist < 4.0 Then
									If Players(ttmp-1)\VisibilityTimer = 30 Then PlaySound(HorrorSFX(Rand(0, 9)))
									AmbientLight 255,255,255
									If Players(ttmp-1)\VisibilityTimer = 1 Then UpdateLights()
								EndIf
								
								If Players(ttmp-1)\Act = ACT_FLASH_2 And Players(ttmp-1)\VisibilityTimer > 0 And dist < 8.0 Then
									If Players(ttmp-1)\VisibilityTimer = 60 Then PlaySound(HorrorSFX(Rand(0, 9)))
									EntityFX GetChild(Players(ttmp-1)\Pivot,1),8
									If Players(ttmp-1)\VisibilityTimer = 1 Then EntityFX GetChild(Players(ttmp-1)\Pivot,1),0
								EndIf
								
								
								If Players(ttmp-1)\Act = ACT_WALK And Players(ttmp-1)\VisibilityTimer > 0 And dist < 8.0 Then
									If Players(ttmp-1)\VisibilityTimer = 300 Then PlaySound(HorrorSFX(Rand(0, 9)))
									AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
									If Players(ttmp-1)\VisibilityTimer = 1 Then UpdateLights()
								EndIf
								
								If Players(ttmp-1)\Act = ACT_RUN And dist < 8.0 Then
									temptimer = Players(ttmp-1)\VisibilityTimer
									
									If temptimer  = 360 Or temptimer = 240 Or temptimer = 120 Then
										If temptimer = 360 Then PlaySound(HorrorSFX(Rand(0, 9)))
										CameraFogRange camera, 1, 5.4
										AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
									EndIf
									
									If temptimer = 300 Or temptimer = 170 Or temptimer = 50 Then
										If temptimer = 300 Then PlaySound(HorrorSFX(Rand(0, 9)))
										CameraFogRange camera, 1, 10
										AmbientLight 255,255,255
									EndIf
									
									If temptimer = 1 Then
										CameraFogRange camera, 1, 5.4
										AmbientLight BRIGHTNESS,BRIGHTNESS,BRIGHTNESS
										UpdateLights()
									EndIf
								EndIf
								
								If Players(ttmp-1)\Act = ACT_173 And Players(ttmp-1)\VisibilityTimer > 0 And dist < 8.0 Then
									If ChannelPlaying(SoundChannel) = False And Players(ttmp-1)\VisibilityTimer > 120 Then SoundChannel = PlaySound(DontlookSFX)
									EntityFX GetChild(Players(ttmp-1)\Pivot,1),8
									If Players(ttmp-1)\VisibilityTimer = 1 Then  EntityFX GetChild(Players(ttmp-1)\Pivot,1),0
								Else
								EndIf
								
								If Players(ttmp-1)\Act = ACT_BEHIND And Players(ttmp-1)\VisibilityTimer > 0 And dist < 8.0 Then
									If Players(ttmp-1)\VisibilityTimer = 300 Then PlaySound(BehindSFX)
									EntityFX GetChild(Players(ttmp-1)\Pivot,1),8
									If Players(ttmp-1)\VisibilityTimer = 1 Then  EntityFX GetChild(Players(ttmp-1)\Pivot,1),0
								EndIf
								
								If Players(ttmp-1)\Act = ACT_BLUR And Players(ttmp-1)\VisibilityTimer > 0 And dist < 8.0 Then
									If ChannelPlaying(SoundChannel) = False And Players(i)\VisibilityTimer > 600 Then SoundChannel = PlaySound(EyekillerSFX)
									EntityFX GetChild(Players(ttmp-1)\Pivot,1),8
									If EntityVisible(Players(ttmp-1)\Pivot, camera) Then BlurTimer = 270
								EndIf
								
								If Players(ttmp-1)\Act = ACT_DARKNESS And Players(ttmp-1)\VisibilityTimer > 0 And dist < 5.0 Then
									If Players(ttmp-1)\VisibilityTimer = 300 Then PlaySound(FireOff) : BlurTimer = 270 : AmbientLight 10,10,10
									If Players(ttmp-1)\VisibilityTimer = 50 Then PlaySound(FireOn)
									If Players(ttmp-1)\VisibilityTimer = 1 Then UpdateLights()
								EndIf
						EndIf
						

					Else
						Players(i)\Connected = False
						PositionEntity Players(i)\Pivot,0.0,100.0,0.0,True
						RotateEntity Players(i)\Pivot,0.0,0.0,0.0,True
						ResetEntity Players(i)\Pivot
					EndIf
				Next
				
				Local latestSpook% = ReadInt(Server)
				If latestSpook>LastRecvSpook Then
					DebugLog "AHHHH " +LastRecvSpook+ " " +latestSpook
					LastRecvSpook = latestSpook
					SpookTimer = 40
				EndIf
				
				ttmp% = ReadByte(Server)
				For i=1 To ttmp
					Local msgID% = ReadInt(Server)
					Local msgTxt$ = ReadLine(Server)
					Local red% = ReadByte(Server)
					Local green% = ReadByte(Server)
					Local blue% = ReadByte(Server)
					If msgID>RecvChatID Then
						RecvChatID = msgID
						AddChatMsg(msgTxt,red,green,blue)
					EndIf
				Next
			EndIf
			getconn = RecvUDPMsg(Server)
		Wend
		For i=0 To 31
			If Players(i)<>Null And i<>ID And Players(i)\Connected Then

				If Players(i)\KillTimer<=0 Then
					If Players(i)\MForw+Players(i)\MBack+Players(i)\MLeft+Players(i)\MRight>0 Then
						animationtime# = 0.2
						If Players(i)\MShift = True Then animationtime = 0.2*1.7
						
						SetAnimTime GetChild(Players(i)\Pivot,1),AnimTime(GetChild(Players(i)\Pivot,1))+animationtime
						If AnimTime(GetChild(Players(i)\Pivot,1)) >= 14.0 Then
							SetAnimTime GetChild(Players(i)\Pivot,1),1
						EndIf
					Else
						If Players(i)\Act = ACT_173 And Players(i)\VisibilityTimer > 0 Then
							Animate2(GetChild(Players(i)\Pivot,1), AnimTime(GetChild(Players(i)\Pivot,1)), 205, 299, 0.08)
						Else
							SetAnimTime GetChild(Players(i)\Pivot,1),AnimStandTime
						EndIf
					EndIf
				Else
					If AnimTime(GetChild(Players(i)\Pivot,1)) = 301.0 Then SetAnimTime GetChild(Players(i)\Pivot,1),166	
					SetAnimTime GetChild(Players(i)\Pivot,1),min(max(AnimTime(GetChild(Players(i)\Pivot,1))+0.1,166),173)
				EndIf
				ScaleEntity GetChild(Players(i)\Pivot,1),0.15,0.15,0.15,True
			EndIf
		Next
		If MessageSendToServer <> "" Then
			WriteLine Server,Str(MessageSendToServer)
			WriteByte Server,ID
		Else
			WriteLine Server,Str(ID)
			WriteByte Server,ID
		EndIf
		MessageSendToServer = ""
		WriteInt Server,SentMsgID
		;WriteByte Server,(MoveForw)+(MoveBack*2)+(MoveLeft*4)+(MoveRight*8) ;send all movement keypresses in one byte
		For i=0 To 59
			Local aaa% = DirListShift-i
			If aaa<0 Then aaa=aaa+60
			WriteByte Server,DirList[aaa]
			WriteFloat Server,DirListAngle[aaa]
		Next
		;WriteFloat Server,EntityYaw(collider,True)
		WriteInt Server,SentChatMsgID
		WriteLine Server,SentChatMsg
		WriteInt Server,RecvChatID
		SendUDPMsg(Server,Players(0)\IP,Players(0)\Port)
		SentMsgID=SentMsgID+1
		If (MilliSecs()-Players(0)\LastMsgTime>AUTO_DISCONNECT_TIME) Then ;disconnect after X seconds of inactivity: assume connection was unexpectedly lost
			RuntimeError("Lost connection to the server")
			AddChatMsg("Lost connection to the server!",255,0,0)
			For i=0 To 31
				If i<>ID Then
					If Players(i)<>Null Then
						FreeEntity GetChild(Players(i)\Pivot,1)
						FreeEntity Players(i)\Pivot
						Delete Players(i)
						Players(i) = Null
					EndIf
				EndIf
				PlayState = GAME_SOLO
			Next
			Paused = True
			KeyHit1 = True
		EndIf
		
	EndIf
	
	If IsFlying And VisibilityTimer > 0 Then
		IsFlying = False
	EndIf
	
	If PlayingAs <> PLAYER_CLASSD Then
	
		If VisibilityTimer<=0 Then
			Select PlayingAs
				Case PLAYER_MENTAL
					AmbientLight 150,150,255
				Case PLAYER_REDMIST
					AmbientLight 255,150,150
			End Select
			EntityType collider,hit_invisible
			If IsFlying Then EntityType collider,hit_map
		Else
			AmbientLight 255,255,255
			EntityType collider,hit_monster
		EndIf
		For i=(PlayState=GAME_SERVER) To 31
			If Players(i)<>Null Then
				If Players(i)\PlayingAs<>PLAYER_CLASSD And Players(i)\VisibilityTimer<0 Then
					EntityAlpha(GetChild(Players(i)\Pivot,1),0.4)
				Else
					EntityAlpha(GetChild(Players(i)\Pivot,1),1.0)
				EndIf
				
				If Players(i)\IsFlying And Players(i)\VisibilityTimer > 0 Then
					Players(i)\IsFlying = False
				EndIf
			EndIf
		Next
		
	Else
		
		For i=(PlayState=GAME_SERVER) To 31
			If Players(i)<>Null Then
				If Players(i)\PlayingAs<>PLAYER_CLASSD And Players(i)\VisibilityTimer<0 Then
					EntityAlpha(GetChild(Players(i)\Pivot,1),0.0)
				Else
					EntityAlpha(GetChild(Players(i)\Pivot,1),1.0)
				EndIf
			EndIf
		Next
		
	EndIf
	
	If VisibilityTimer < 0 Then
		CanMove = True
	ElseIf Act=ACT_FLASH Then
		canmove = False
	EndIf
	
	If (Act = ACT_FLASH) And VisibilityTimer = 30 Then
		PlaySound(HorrorSFX(Rand(0, 9)))
	EndIf
	
	If (Act = ACT_FLASH_2) And VisibilityTimer = 60 Then
		PlaySound(HorrorSFX(Rand(0, 9)))
	EndIf
	
	If (Act = ACT_BEHIND) And VisibilityTimer = 300 Then
		PlaySound(BehindSFX)
	EndIf
	
	If Act = ACT_WALK And VisibilityTimer = 300 Then
		PlaySound(HorrorSFX(Rand(0, 9)))
	EndIf
	
	If Act = ACT_RUN Then
		If VisibilityTimer = 360 Or VisibilityTimer = 240 Or VisibilityTimer = 120 Then
			If VisibilityTimer = 360 Then PlaySound(HorrorSFX(Rand(0, 9)))
			CameraFogRange camera, 1, 3.8
			AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
		EndIf
		
		If VisibilityTimer = 300 Or VisibilityTimer = 170 Or VisibilityTimer = 50 Then
			If VisibilityTimer = 300 Then PlaySound(HorrorSFX(Rand(0, 9)))
			CameraFogRange camera, 1, 15
			AmbientLight Brightness,Brightness,Brightness
		EndIf
		
	EndIf
	
	If Act = ACT_173 And VisibilityTimer > 0 Then
		
		CanMove = True
		For i=(PlayState=GAME_SERVER) To 31
			If Players(i)<>Null And i <> ID Then
				If Players(i)\PlayingAs=PLAYER_CLASSD And Players(i)\Connected Then
					vXa# = EntityX(collider)-EntityX(Players(i)\Pivot)
					vZa# = EntityZ(collider)-EntityZ(Players(i)\Pivot)
					vLena# = Sqr((vXa*vXa)+(vZa*vZa))
					vYawa# = VectorYaw(vXa/vLena,0.0,vZa/vLena)-EntityYaw(Players(i)\Pivot)
					While vYawa<-180.0
						vYawa=vYawa+360.0
					Wend
					While vYaw>=180.0
						vYawa=vYawa-360.0
					Wend
					currfloor = (-EntityY(Players(i)\Pivot)-0.3)/2+0.1
					If Abs(PlayerFloor-currfloor) <= 1 And Abs(vYawa)<45 And EntityVisible(collider,Players(i)\Pivot) Then
						CanMove = False
					EndIf
				EndIf
			EndIf
		Next
		If ChannelPlaying(SoundChannel) = False Then SoundChannel = PlaySound(DontlookSFX)
	EndIf
	
	If Act = ACT_BEHIND And VisibilityTimer > 0 Then
		
		CanMove = False
		For i=(PlayState=GAME_SERVER) To 31
			If Players(i)<>Null And i <> ID Then
				If Players(i)\PlayingAs=PLAYER_CLASSD And Players(i)\Connected Then
					vXa# = EntityX(collider)-EntityX(Players(i)\Pivot)
					vZa# = EntityZ(collider)-EntityZ(Players(i)\Pivot)
					vLena# = Sqr((vXa*vXa)+(vZa*vZa))
					vYawa# = VectorYaw(vXa/vLena,0.0,vZa/vLena)-EntityYaw(Players(i)\Pivot)
					While vYawa<-180.0
						vYawa=vYawa+360.0
					Wend
					While vYaw>=180.0
						vYawa=vYawa-360.0
					Wend
					currfloor = (-EntityY(Players(i)\Pivot)-0.3)/2+0.1
					If Abs(PlayerFloor-currfloor) <= 1 And Abs(vYawa)<45 And EntityVisible(collider,Players(i)\Pivot) Then
						CanMove = True
					EndIf
				EndIf
			EndIf
		Next
		
	EndIf
	
	If Act = ACT_BLUR And VisibilityTimer > 0 Then
		
		CanMove = False
		If ChannelPlaying(SoundChannel) = False And VisibilityTimer > 600 Then SoundChannel = PlaySound(EyekillerSFX)
	EndIf
	
	SpookTimer=max(SpookTimer-1,0)
	If PlayingAs<>PLAYER_CLASSD;PlayState=GAME_SERVER And PlayingAs<>PLAYER_CLASSD Then
		
		If VisibilityTimer<0 And SpookCount>0 Then SpookCount=3
		If VisibilityTimer<=-SCARE_COOLDOWN Then SpookCount=0
		
		If MouseHit1 Then
			If VisibilityTimer>0 And Act <> ACT_FLASH And Act <> ACT_BLUR And Act <> ACT_FLASH_2 Then
				DebugLog "lclick"
				For i=1 To 31
					If Players(i)<>Null Then
						If Players(i)\PlayingAs=PLAYER_CLASSD Then
							vXa# = EntityX(Players(i)\Pivot)-EntityX(collider)
							vZa# = EntityZ(Players(i)\Pivot)-EntityZ(collider)
							vLena# = Sqr((vXa*vXa)+(vZa*vZa))
							vYawa# = VectorYaw(vXa/vLena,0.0,vZa/vLena)-EntityYaw(collider)
							While vYawa<-180.0
								vYawa=vYawa+360.0
							Wend
							While vYaw>=180.0
								vYawa=vYawa-360.0
							Wend
							;DebugLog "ANGLE " +Abs(vYawa)
							If EntityInView(GetChild(Players(i)\Pivot,1),camera) And EntityDistance(Players(i)\Pivot,collider)<0.8 And Abs(vYawa)<45 Then
								If Players(i)\KillTimer<=0 Then AddChatMsg(PlayerName+ " killed " +Players(i)\Name+ "!",255,100,0,i,True)
								Players(i)\KillTimer = max(Players(i)\KillTimer,1)
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		EndIf
		
		If MouseHit2 Then
			DebugLog "rclick"
			If SpookCount<3 And SpookCooldown<=0 Then
				If Act = ACT_FLASH Then
					VisibilityTimer = 30+1
					SpookCount=SpookCount+1
					SpookTimer=40
					SpookCooldown=95
					CanMove = False
				EndIf
				
				If Act = ACT_WALK Then
					VisibilityTimer = 300+1
					SpookCount=3
					SpookTimer=40
					SpookCooldown=95
				EndIf
				
				If Act = ACT_RUN Then
					VisibilityTimer = 360+1
					SpookCount=3
					SpookTimer=360
					SpookCooldown=95
				EndIf
				
				If Act = ACT_173 Then
					VisibilityTimer = 1200+1
					SpookCount=3				
					SpookTimer=40
					SpookCooldown=95
				EndIf
				
				If Act = ACT_FLASH_2 Then
					VisibilityTimer = 60+1
					SpookCount=SpookCount+1
					SpookTimer=40
					SpookCooldown=95
				EndIf
				
				If Act = ACT_BEHIND Then
					VisibilityTimer = 300+1
					SpookCount=3
					SpookTimer=40
					SpookCooldown=95
				EndIf
				
				If Act = ACT_BLUR Then
					VisibilityTimer = 1200+1
					SpookCount=3
					SpookTimer=40
					SpookCooldown=95
				EndIf
				
				If Act = ACT_DARKNESS Then
					VisibilityTimer = 300+1
					SpookCount=3
					SpookTimer=40
					SpookCooldown=95
				EndIf
				
				If PlayState=GAME_SERVER Then
				
				LastSentSpook=LastSentSpook+1
				For i=1 To 31
					If Players(i)<>Null Then
						If EntityDistance(Players(i)\Pivot,collider)<8.0 Then 
							Players(i)\LastSpookSent = LastSentSpook
						EndIf
					EndIf
				Next
				
				EndIf
			EndIf
		EndIf
	EndIf

	MoveForw = False
	MoveBack = False
	MoveLeft = False
	MoveRight = False

	
	Local updatelight% = False ;Fix this
	
	For i=0 To 31
		If Players(i)<>Null Then
		
			currfloor = (-EntityY(Players(i)\Pivot)-0.3)/2+0.1
			If i=ID Then currfloor = PlayerFloor
			
			If PlayState = GAME_SERVER And currfloor >= 100 Then
				If i=ID Then
					PositionEntity collider,-2.5,ColliderYDefault#, -0.5,True
					dropspeed = 0.0
				Else
					PositionEntity Players(i)\Pivot,-2.5,ColliderYDefault#, -0.5,True
					Players(i)\FallSpeed = 0.0
				EndIf
			EndIf
			
			If i <> 0 Then
				If Players(i)\PlayingAs<>PLAYER_CLASSD And Players(i)\VisibilityTimer>0 Then
					updatelight = False
				EndIf
			EndIf
		EndIf
	Next
	
	If PlayingAs <> PLAYER_CLASSD And VisibilityTimer > 0 Then updatelight = False

	If updatelight Then UpdateLights()

End Function

Function AddChatMsg(txt$,r%,g%,b%,player%=-1,glbal%=False,time%=15000)
	MsgCount=MsgCount+1
	If Len(txt)>200 Then
		txt = Left(txt,188)+ "- [REDACTED]"
	EndIf
	Local cm.ChatMessage
	Local cmCount% = 0
	For cm = Each ChatMessage
		If PlayState=GAME_SERVER Then
			If cm\SendTo[0] Then cmCount=cmCount-1
		EndIf
		cmCount=cmCount+1
	Next
	While cmCount>=20
		Delete (First ChatMessage)
		cmCount=cmCount-1
	Wend
	cm = New ChatMessage
	cm\ID = MsgCount
	cm\Txt = txt
	cm\R = r
	cm\G = g
	cm\B = b
	cm\Timer = MilliSecs()+time%
	cm\Sender = player
	
	For i%=0 To 31
		cm\SendTo[i]=False
	Next
	If player<0 Or glbal Then
		For i%=0 To 31
			cm\SendTo[i]=True
		Next
	EndIf
	
	cm\SendTo[ID] = True
	
	;If ChatLogFile = 0 Then ; idk how to fix
	;	Local clogID% = 1
	;	While FileType("Chatlogs\chatlog" + clogID + ".txt") <> 0
	;		clogID = clogID + 1
	;	Wend
	;	ChatLogFile = WriteFile("Chatlogs\chatlog" + clogID + ".txt")
	;EndIf
	
	;WriteLine(ChatLogFile, cm\Txt)
End Function

Function UpdateChatMsgs()
	Local cm.ChatMessage
	Local msgAmount% = 1
	Local y% = 0 - (25*ChatOpened)
	For cm = Each ChatMessage
		If cm\Timer>MilliSecs() Or IsPaused Or ChatOpened Then
			msgAmount=msgAmount+1
		EndIf
	Next
	For cm = Each ChatMessage
		If cm\timer > MilliSecs() Or IsPaused Or ChatOpened Then
			If PlayState=GAME_SERVER Then
				Local txt$ = cm\Txt
				If AdminSpyChat Then
					txt="(" +cm\Sender+ ") " +txt
				EndIf
				Color 0,0,0
				Text 6,GraphicsHeight()-(20*(msgAmount-1))+y+1,txt
				If cm\SendTo[0] Or AdminSpyChat Then
					If cm\SendTo[0] Then
						Color cm\R,cm\G,cm\B
					Else
						Color 150,150,150
					EndIf
					
					If cm\timer < MilliSecs()+3000 And (Not IsPaused) And (Not ChatOpened) Then
						red = max(0,cm\R*((cm\timer-MilliSecs())/3000.0))
						blue = max(0,cm\B*((cm\timer-MilliSecs())/3000.0))
						green = max(0,cm\G*((cm\timer-MilliSecs())/3000.0))
						Color red,green,blue
					Else
						Color cm\R,cm\G,cm\B
					EndIf
					
					Text 5,GraphicsHeight()-(20*(msgAmount-1))+y,txt
					Color 255,255,255
					y=y+20
				EndIf
			Else
				Color 0,0,0
				Text 6,GraphicsHeight()-(20*(msgAmount-1))+y+1,cm\Txt
				If cm\timer < MilliSecs()+3000 And (Not IsPaused) And (Not ChatOpened) Then
					red = max(0,cm\R*((cm\timer-MilliSecs())/3000.0))
					blue = max(0,cm\B*((cm\timer-MilliSecs())/3000.0))
					green = max(0,cm\G*((cm\timer-MilliSecs())/3000.0))
					Color red,green,blue
				Else
					Color cm\R,cm\G,cm\B
				EndIf
				Text 5,GraphicsHeight()-(20*(msgAmount-1))+y,cm\Txt
				Color 255,255,255
				y=y+20
			EndIf
		EndIf
	Next
End Function

Function UpdateLights()
	If PlayingAs = PLAYER_MENTAL Then
		AmbientLight 150,150,250
		CameraFogRange camera,1,30
		CameraRange camera,0.001,30
		CameraFogColor camera,100,100,250
	ElseIf PlayingAs = PLAYER_REDMIST Then
		AmbientLight 255,150,150
		CameraFogRange camera,1,30
		CameraRange camera,0.001,30
		CameraFogColor camera,250,100,100
	ElseIf PlayingAs = PLAYER_EYEKILLER
		AmbientLight 130,50,255
		CameraFogRange camera,1,30
		CameraRange camera,0.001,30
		CameraFogColor camera,130,50,255
	Else
		AmbientLight Brightness,Brightness,Brightness
		CameraFogRange camera,1,6
		CameraRange camera,0.001,10
		CameraFogColor camera,70,70,70
	EndIf
	CameraRange camera, 0.001,20
End Function


Function FloatEquals%(a#,b#)
	Return Abs(a-b)<0.3
End Function