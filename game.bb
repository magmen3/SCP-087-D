;-------------------------------------------------------------;
;--------------------   SCP - 087 - D   ----------------------;
;---------        Modification of SCP-087-B        -----------;
;---------   Based on SCP-087-B Extended Edition   -----------;
;-------------------------------------------------------------;


Graphics3D 640, 480, 32, 2

Global GAME_VERSION$ = "v0.35"

AppTitle "SCP 087-D " + GAME_VERSION

Global ButtonSFX = LoadSound("SFX\Button.ogg")
Dim ChosenButton(5)
ChosenButton(0) = 0
ChosenButton(1) = 0
ChosenButton(2) = 0
ChosenButton(3) = 0

Global MapString$
MapString = ""

Include "server.bb"
Include "scancode.bb"

Global SelectedInputBox = 0

Global JoinToServer = False

Global MenuFont1%,MenuFont2%
Global MouseHit1%,MouseHit2%
;Global PointerImg%
Global launchertex%
Global BRIGHTNESS_MAX% = 140 ; 200
Global DO_ANIMATION% = True
;PointerImg = LoadImage("GFX\cursor.PNG")
;MaskImage PointerImg,0,0,0
MenuFont1 = LoadFont("OCR A Becker RUS-LAT.TTF", 13)
MenuFont2 = LoadFont("OCR A Becker RUS-LAT.TTF", 16)

Global screenwidth = GetINIInt("options.ini","options","width")
Global screenheight = GetINIInt("options.ini","options","height")
Global colordepth = GetINIInt("options.ini","options","colordepth")
Global fullscreen = max(GetINIInt("options.ini","options","fullscreen"), (Not Windowed3D()))
Global InvertMouse =  GetINIInt("options.ini","options","invert mouse y")
Global Brightness=max(min(GetINIInt("options.ini","options","brightness"), BRIGHTNESS_MAX), 10)
Global Launcher  = GetINIInt("options.ini","options","launcher")
Global framelimit = GetINIInt("options.ini","options","framelimit")
Global skipintro = GetINIInt("options.ini","options","skipintro")
Global area$ = GetINIString("options.ini","options","area")
Global sector$ = GetINIString("options.ini","options","sector")
Global skipupdate = GetINIInt("options.ini","options","skip auto-update")

If Launcher Then

SetBuffer BackBuffer()

SetFont MenuFont1
ClsColor 0,0,0
Cls
Color 255,255,255

Local selectedGFXmode%
selectedGFXmode=1

For i = 1 To CountGfxModes3D()
	If (GfxMode3D(i) And GfxModeDepth(i) = colordepth) Then
		If (GfxModeWidth(i)>=screenwidth And GfxModeHeight(i)>=screenheight) Then
			selectedGFXmode=i
			Exit
		EndIf
		selectedGFXmode=i
	EndIf
Next
screenwidth = GfxModeWidth(selectedGFXmode)
screenheight = GfxModeHeight(selectedGFXmode)
PutINIValue("options.ini", "options", "width", Str(screenwidth))
PutINIValue("options.ini", "options", "height", Str(screenheight))

launchertex =		 LoadImage("GFX\launcher.jpg")
launchertex_a1 =	 LoadImage("GFX\launcher_a1.png")
launchertex_a2 =	 LoadImage("GFX\launcher_a2.png")
launchertex_a1_s1 =  LoadImage("GFX\launcher_a1_s1.png")
launchertex_a1_s2 =  LoadImage("GFX\launcher_a1_s2.png")
launchertex_s1 = 	 LoadImage("GFX\launcher_s1.png")
launchertex_s2 = 	 LoadImage("GFX\launcher_s2.png")


While True
	Local StrTemp$
	SetFont MenuFont1
	MouseHit1 = MouseHit(1)
	MouseHit2 = MouseHit(2)
	Cls

	If MultiplayerButton = False Then
		
		If area = "The Second" Then
			DrawImage launchertex_a2,0,0
			Color 0,0,0
			Text 640-129,480-345,"Area:"
			Color 255,255,255
			Text 640-129,480-346,"Area:"
			
			Color 30,30,30
			StrTemp = "The Second"
			If (Button(640-87,480-347,85,24,StrTemp,False)) Then
				area = "The First"
				PutINIValue("options.ini","options","area",Str(area))
			EndIf
	
		ElseIf area = "The First" Then
			If sector = "Lower" Then
				DrawImage launchertex_a1_s2,0,0
			Else
				DrawImage launchertex_a1_s1,0,0
			EndIf
			
			
			Color 0,0,0
			Text 640-129,480-345,"Area:"
			Color 255,255,255
			Text 640-129,480-346,"Area:"
			
			Color 30,30,30
			StrTemp = "The First"
			If (Button(640-87,480-347,85,24,StrTemp,False)) Then
				area = "The Second"
				PutINIValue("options.ini","options","area",Str(area))
			EndIf
			
			Color 0,0,0
			Text 640-139,480-315,"Sector:"
			Color 255,255,255
			Text 640-139,480-316,"Sector:"
			
			If sector = "Upper" Then
				Color 30,30,30
				StrTemp = "Upper"
				If (Button(640-87,480-317,85,24,StrTemp,False)) Then
					sector = "Lower"
					PutINIValue("options.ini","options","sector",Str(sector))
				EndIf
			Else
				Color 30,30,30
				StrTemp = "Lower"
				If (Button(640-87,480-317,85,24,StrTemp,False)) Then
					sector = "Upper"
					PutINIValue("options.ini","options","sector",Str(sector))
				EndIf
			EndIf
			
		Else
			If sector = "Upper" Then
				DrawImage launchertex_s1,0,0
				
				Color 0,0,0
				Text 640-139,480-315,"Sector:"
				Color 255,255,255
				Text 640-139,480-316,"Sector:"
				
				Color 30,30,30
				StrTemp = "Upper"
				If (Button(640-87,480-317,85,24,StrTemp,False)) Then
					sector = "Lower"
					PutINIValue("options.ini","options","sector",Str(sector))
				EndIf
			ElseIf sector = "Lower" Then
				DrawImage launchertex_s2,0,0
				
				Color 0,0,0
				Text 640-139,480-315,"Sector:"
				Color 255,255,255
				Text 640-139,480-316,"Sector:"
				
				Color 30,30,30
				StrTemp = "Lower"
				If (Button(640-87,480-317,85,24,StrTemp,False)) Then
					sector = "Upper"
					PutINIValue("options.ini","options","sector",Str(sector))
				EndIf
			Else
				DrawImage launchertex,0,0
			EndIf
			
		EndIf
			
			Color 30,30,30
			If (Button(640-336,480-32,124,24,"MULTIPLAYER",False)) Then
				MultiplayerButton = True
			EndIf
			
			Color 30,30,30
			If Button(640-100,480-32,100,24,"PLAY") Then Flip : Delay 8 : Exit
			Color 30,30,30
			If Button(640-206,480-32,100,24,"EXIT") Then Flip : Delay 8 : End
		
	Else
		DrawImage launchertex,0,0
		
		Color 0,0,0
		Text 640-357,480-63,"IP:"
		Color 255,255,255
		Text 640-357,480-64,"IP:"
		
		ServerAddress$ = InputBox(640-336,480-68,114,24,ServerAddress$,1,True)
		
		Color 0,0,0
		Text 640-379,480-93,"Name:"
		Color 255,255,255
		Text 640-379,480-94,"Name:"
		
		SetFont MenuFont2
		If Len(PlayerName$) > 12 Then
			PlayerName$ = InputBox(640-336,480-98,114,24,Mid(PlayerName$,1,12),2,True)
		Else
			PlayerName$ = InputBox(640-336,480-98,114,24,PlayerName$,2,True)
		EndIf
		SetFont MenuFont1
		Color 30, 30, 30
		If (Button(640-336,480-32,124,24,"SINGLEPLAYER",False)) Then
			MultiplayerButton = False
		EndIf
		
		Color 30,30,30
		If Button(640-100,480-32,100,24,"HOST SERVER") Then 
			PutINIValue("options.ini","multiplayer","name",Str(PlayerName$))
			PlayState=GAME_SERVER ;let the game know you're a server
			Players(0)=New Player
			Players(0)\IP = 1 ;for initialize in UpdateServer() function
			Exit
		EndIf
		Color 30,30,30
		If Button(640-206,480-32,100,24,"JOIN SERVER") Then
			PutINIValue("options.ini","multiplayer","name",Str(PlayerName$))
			PutINIValue("options.ini","multiplayer","ip",Str(ServerAddress$))
			ConnectToServer(ServerAddress)
			If PlayState = GAME_CLIENT Then Exit
		EndIf
		
	EndIf

	
	Color 255,255,255
	
	Local dx%,dy%
	dx = 0 : dy = 0
	
	Color 0,0,0
	Text 36,101,"Resolution:"
	Color 255,255,255
	Text 35,100,"Resolution:"
	
	For i=1 To CountGfxModes3D()
		If (GfxMode3D(i)) Then
			If (GfxModeDepth(i) = colordepth) Then
				If selectedGFXmode = i Then Color 30,30,100 Else Color 30,30,30
				If (Button(dx+35,dy+120,80,20,Str(GfxModeWidth(i))+ "x" +Str(GfxModeHeight(i)),False)) Then
					selectedGFXmode=i
					screenwidth=GfxModeWidth(i)
					screenheight=GfxModeHeight(i)
					PutINIValue("options.ini","options","width",Str(screenwidth))
					PutINIValue("options.ini","options","height",Str(screenheight))
				EndIf
				dy=dy+20
				If (dy>=240) Then
					dx=dx+80
					dy=0
				EndIf
			EndIf
		EndIf
	Next
	
	Color 0,0,0
	Text 36,480-53,"Brightness:"
	Color 255,255,255
	Text 35,480-54,"Brightness:"
	
	Rect 35,480-22,BRIGHTNESS_MAX,5,False
	Color 0,0,0
	Rect 34,480-23,BRIGHTNESS_MAX+2,7,False
	
	Color 0,0,50
	Button(Brightness+20,480-30,40,20,Str(Brightness))
	If (MouseX()>=35 And MouseX()<=35+BRIGHTNESS_MAX-10 And MouseY()>=480-30 And MouseY()<=480-10 And MouseDown(1)) Then
		Brightness=MouseX()-25
		PutINIValue("options.ini","options","brightness",Str(Brightness))
	EndIf
	
	Color 0,0,0
	Text 640-129,480-95,"Color depth:"
	If fullscreen Then Color 255,255,255 Else Color 100,100,100
	Text 640-130,480-96,"Color depth:"
	Color 30,30,30
	If (Button(640-46,480-100,44,24,Str(colordepth)+ "-bit",(Not fullscreen))) Then
		If (colordepth=32) Then
			colordepth=16
		Else
			colordepth=32
		EndIf
		PutINIValue("options.ini","options","colordepth",Str(colordepth))
		For i=1 To CountGfxModes3D()
			If (GfxMode3D(i) And GfxModeDepth(i) = colordepth) Then
				If (GfxModeWidth(i)>=screenwidth And GfxModeHeight(i)>=screenheight) Then
					selectedGFXmode=i
					Exit
				EndIf
				selectedGFXmode=i
			EndIf
		Next
	EndIf
	
	Color 0,0,0
	Text 640-129,480-65,"Mode:"
	Color 255,255,255
	Text 640-130,480-66,"Mode:"
	Color 30,30,30
	If (fullscreen) Then StrTemp = "Fullscreen" Else StrTemp = "Window"
	If (Button(640-84,480-70,82,24,StrTemp,(Not Windowed3D()))) Then
		fullscreen=(Not fullscreen)
		PutINIValue("options.ini","options","fullscreen",Str(fullscreen))
	EndIf
	
	Color 0,0,0
	Text 640-129,480-125,"Framelimit:"
	Color 255,255,255
	Text 640-130,480-126,"Framelimit:"
	Color 30,30,30
	
	Color 0,0,0
	Text 640-129,480-155,"Skip intro:"
	Color 255,255,255
	Text 640-130,480-156,"Skip intro:"
	Color 30,30,30

	If skipintro Then StrTemp = "Yes" Else StrTemp = "No"
	If (Button(640-46,480-160,44,24,StrTemp,False)) Then
		skipintro = Not skipintro
		PutINIValue("options.ini","options","skipintro",skipintro)
	EndIf

	Color 30,30,30
	StrTemp = framelimit
	If (Button(640-46,480-130,44,24,StrTemp,False)) Then
		If framelimit >= 60 Then
			framelimit = 30
		Else
			framelimit = framelimit + 15
		EndIf
		PutINIValue("options.ini","options","framelimit",framelimit)
	EndIf
	
	Color 255,255,255
	
	Flip
	Delay 6
Wend
EndIf

screenwidth = GetINIInt("options.ini","options","width")
screenheight = GetINIInt("options.ini","options","height")
colordepth = GetINIInt("options.ini","options","colordepth")
fullscreen = GetINIInt("options.ini","options","fullscreen")
framelimit = GetINIInt("options.ini","options","framelimit")
skipintro = GetINIInt("options.ini","options","skipintro")

If fullscreen Then
	Graphics3D (screenwidth,screenheight,colordepth)
Else
	Graphics3D (screenwidth,screenheight,colordepth,2)
EndIf

AntiAlias True 
HidePointer 

SetBuffer BackBuffer()

ClsColor 0,0,0
Cls
Local tempImg%

tempImg = LoadImage("GFX\scp.jpg")
DrawImage(tempImg,(GraphicsWidth()/2)-(ImageWidth(tempImg)/2),(GraphicsHeight()/2)-(ImageHeight(tempImg)/2))
Flip
FreeImage tempImg

Global LogoImg = LoadImage("GFX\scplogo.png")
ScaleImage(LogoImg,GraphicsWidth() / 1280.0, GraphicsHeight() / 720.0)

Global font1 	= LoadFont("OCR A Becker RUS-LAT.TTF", 24) ;LoadFont("GFX\YouMurderer BB.TTF", 24)
Global font	 	= LoadFont("OCR A Becker RUS-LAT.TTF", 128) ;LoadFont("GFX\YouMurderer BB.TTF", 128)
Global signfont	= LoadFont("GFX\YouMurderer BB.TTF", 128)

Const hit_map = 1
Const hit_map2 = 5
Const hit_monster = 2
Const hit_friendly = 3
Const hit_invisible = 4
Const hit_player = 6

SeedRnd MilliSecs()

; -- Viewport.
Global viewport_center_x = GraphicsWidth () / 2
Global viewport_center_y = GraphicsHeight () / 2
;^^^^^^

; -- Mouselook.
Global mouselook_x_inc# = 0.3 ; This sets both the sensitivity and direction (+/-) of the mouse on the X axis.
Global mouselook_y_inc# = 0.3 ; This sets both the And direction (+/-) of the mouse on the Y axis.
Global mouse_left_limit = 250 ; Used to limit the mouse movement to within a certain number of pixels (250 is used here) from the center of the screen. This produces smoother mouse movement than continuously moving the mouse back to the center each loop.
Global mouse_right_limit = GraphicsWidth () - 250 ; As above.
Global mouse_top_limit = 250 ; As above.
Global mouse_bottom_limit = GraphicsHeight () - 250 ; As above.
;^^^^^^

; -- Mouse smoothing que.
Global mouse_x_speed_1#, mouse_x_speed_2#,mouse_x_speed_3#,mouse_x_speed_4#,mouse_x_speed_5#
Global mouse_y_speed_1#,mouse_y_speed_2#,mouse_y_speed_3#,mouse_y_speed_4#,mouse_y_speed_5#

; -- User.
Global user_camera_pitch#
Global up#, side#
Global STAMINA_MAX# = 500.0
Global Stamina# = STAMINA_MAX
Global StaminaOverload# = 0.0
Global StaminaTimer# = 0.0
Global BlurTimer# = 0.0
Global FogTimer# = 0.0
Global ColliderXRadius# = 0.2
Global ColliderYRadius# = 0.72
Global ColliderYRadiusCrouch# = 0.4
Global camshake# = 0.0
Global ColliderDefaultY# = -1.0
Global DropSpeed#
;^^^^^^


Global shake2coeff# = 1.5
Global shakeZ# = 1.5
Global shakeX# = 0.0

Type ENEMIES
	Field obj 		; objekti
	Field collider	; "t?rm?yspallo"
	Field yspeed#	;pudotusnopeus
	Field speed#
	Field oldX 
	Field oldZ
	Field dorotation
	Field steptimer#
	Field soundemitter
	Field playerlastx
	Field playerlastz
	Field playerlastseen
End Type

Type GLIMPSES
	Field obj
End Type 

Type FLOORS
	Field mesh
	Field sign
End Type

Type OBJECTS
	Field mesh
End Type

Global camera
camera = CreateCamera()
CameraRange camera, 0.001, 10
CameraFogMode camera, 1
CameraFogRange camera, 1, 5
CameraFogColor camera,70,70,70
microphone=CreateListener(camera) ; Create listener, make it child of camera
Include "dreamfilter.bb"
Include "bumpmapping.bb"
CreateBlurImage()
;ShowEntity Fog

Global PlayerFloor, KillTimer%

AmbientLight Brightness + 35,Brightness + 25,Brightness + 5 ; 34

Global collider = CreatePivot()
PositionEntity collider,-2.5,-1.3,-0.5
EntityType collider, hit_player
EntityRadius (collider, ColliderXRadius,ColliderYRadius)

Dim FloorNumberTexture(flooramount+1)

;Brick, concrete & sign textures
Global brickwalltexture = LoadTexture("GFX\brickwall.jpg")
Global brickwalltexture_a2 = LoadTexture("GFX\brickwall_a2.jpg")
Global whitetexture = LoadTexture("GFX\white.jpg")
Global sign = LoadImage("GFX\sign.jpg")
Global map, map1, map2, map3
Global btn, btn1, btn2, btn3, btn4, btn5, wall, door, door1, door2, platform1, platform2, label


Global CurrEnemy.ENEMIES, CurrObject, SoundEmitter = CreatePivot()
ScaleEntity SoundEmitter, 0.1,0.1,0.1

Const ACT_STEPS = 1, ACT_LIGHTS = 2, ACT_FLASH = 3, ACT_WALK = 4, ACT_KALLE = 6, ACT_BREATH = 7
Const ACT_PROCEED = 8, ACT_TRAP=9, ACT_173 = 11, ACT_CELL = 12, ACT_LOCK = 13
Const ACT_RADIO2 = 15, ACT_RADIO3 = 16, ACT_RADIO4 = 17, ACT_TRICK1 = 18, ACT_TRICK2 = 19
Const ACT_ROAR = 20, ACT_DARKNESS = 21, ACT_WAIT = 22, ACT_BEHIND = 23, ACT_CHARGE173 = 24, ACT_ENDING = 25, ACT_LIGHT = 26, ACT_ELEVATOR = 27
Const ACT_BLUR = 28, ACT_BROKENROOM = 29, ACT_173_2 = 30, ACT_BIGSTAIRSROOM = 31, ACT_MAZE = 32, ACT_NOTHING = 33, ACT_RUN = 34, ACT_RUN_2 = 35
Const ACT_RUN_3 = 36, ACT_RUN_4 = 37, ACT_ILLUSIONS = 38, ACT_ILLUSIONS_2 = 39, ACT_HALLWAYTRAP = 40, ACT_TRICK3 = 41, ACT_ENDING_2 = 42, ACT_TRANSITION = 43, ACT_ATTENTION = 44
Const ACT_ELEVATOR_START = 45, ACT_ELEVATOR_END = 46, ACT_FLASH_2 = 47, ACT_CELL_2 = 48, ACT_CORNER_TRAP = 49, ACT_MENTAL_TRAP = 50, ACT_TURNAROUND = 51, ACT_UNKNOWN = 52, ACT_SPIDER = 53, ACT_SUDDEN_ATTACK = 54
Const ACT_GORIGHT = 55, ACT_RANDOM_SOUND = 56
;Collisions hit_er,hit_map, 2,3


;-----------------------------------------------------------;
;-----------------      SFX Start      ---------------------;
Dim StepSFX(8)
Dim MonsterStepSFX(4)
Dim RunSFX(8)
Dim RunBreathSFX(3)
For i = 0 To 3
	MonsterStepSFX(i) = Load3DSound("SFX\Monsters\step" + (i + 1) + ".ogg")
Next

For i = 0 To 7
	StepSFX(i) = Load3DSound("SFX\Player\step" + (i + 1) + ".ogg")
	RunSFX(i) = Load3DSound("SFX\Player\run" + (i + 1) + ".ogg")
	SoundVolume StepSFX(i), 0.6
	SoundVolume RunSFX(i), 0.6
Next

For i = 0 To 3
	RunBreathSFX(i) = LoadSound("SFX\Player\breath" + i + ".ogg")
Next

RunBreathSFX(3)=LoadSound("SFX\Player\breath3.ogg")
Global BreathChnl = RunBreathSFX(0), BreathChannel
BreathChannel = PlaySound(0)

loudstepsound=Load3DSound("SFX\Events\loudstep.ogg")
Dim HorrorSFX(9)
For i = 0 To 8
	HorrorSFX(i) = LoadSound("SFX\Horror\horror" + (i + 1) + ".ogg")
Next
Global DeathSFX = LoadSound("SFX\death.ogg")
;Global IntroSFX = LoadSound("SFX\introdoor.ogg") <---- Unused for now, since i can't find out how to use this sound
Global RoarSFX = LoadSound("SFX\Events\roar.ogg")
Global BreathSFX = LoadSound("SFX\Events\breath.ogg")
Global StoneSFX = LoadSound("SFX\Events\stone.ogg")
Global NoSFX = LoadSound("SFX\Monsters\no.ogg")
Global BehindSFX = LoadSound("SFX\Monsters\behind.ogg")
Global WaitSFX = LoadSound("SFX\Monsters\wait.ogg")
Global ColumnmoveSFX = LoadSound("SFX\Events\columnmove.ogg")
Global LightSFX = LoadSound("SFX\Events\light.ogg")
Global ActRadioSFX = LoadSound("SFX\Monsters\act_radio6.ogg")
Global DontlookSFXReverb = LoadSound("SFX\Monsters\dontlook_reverb.ogg")
Global DontlookSFX = LoadSound("SFX\Monsters\dontlook.ogg")
Global DamageSFX = LoadSound("SFX\Player\damage.ogg")
Global Crush1SFX = LoadSound("SFX\Events\crush1.ogg")
Global Crush2SFX = LoadSound("SFX\Events\crush2.ogg")
Global BellSFX = LoadSound("SFX\Events\bell.ogg")
Global EyeKillerSFX = LoadSound("SFX\Monsters\eyekiller.ogg")
Global TransitionSFX = LoadSound("SFX\Events\transition.ogg")
Global SpiderSFX = LoadSound("SFX\Other\spider.ogg")

Global MUSIC_ON = True

Dim AmbientSFX(111)
For i = 0 To 110
	AmbientSFX(i) = Load3DSound("SFX\Ambients\ambient" + (i + 1) + ".ogg")
Next

Dim RadioSFX(5)
For i = 0 To 4
	RadioSFX(i) = LoadSound("SFX\Radio\radio" + (i +  1)+ ".ogg")
Next

Global MusicIntro = LoadSound("SFX\Music\The Beginning.ogg")
Global MusicDeep = LoadSound("SFX\Music\Deep Hell.ogg")
Global MusicSecondArea = LoadSound("SFX\Music\Ice Demon.ogg")
Global MusicElevator = LoadSound("SFX\Music\Elevator.ogg")
Global Music = LoadSound("SFX\Music\Gathering Darkness.ogg"), MusicChannel
Global ChaseSFX = LoadSound("SFX\Events\chase.ogg"), ChaseChannel
Global SoundSFX = Load3DSound("SFX\Monsters\dontlook.ogg"), SoundChannel
Global SoundSFXReverb = Load3DSound("SFX\Monsters\dontlook_reverb.ogg"), SoundChannelReverb
Global StepChannel
Global RadioMusic = Load3DSound("SFX\Radio\radioMusic.ogg")

Global FireOn = LoadSound("SFX\Events\match.ogg")
Global FireOff = LoadSound("SFX\Events\fireout.ogg")

Global TransisionSFX = LoadSound("SFX\Events\transision.ogg")

Dim GoRightSFX(3)
GoRightSFX(0) = LoadSound("SFX\Monsters\dontgoleft.ogg")
GoRightSFX(1) = LoadSound("SFX\Monsters\goright.ogg")
GoRightSFX(2) = LoadSound("SFX\Monsters\youwillstayforever.ogg")

;Ending sounds
Global BellEndingSFX = LoadSound("SFX\Events\bellEnding.ogg")
Global EscapeSFX = LoadSound("SFX\Music\Escape.ogg")
Global LastSecondsOfLifeSFX = LoadSound("SFX\Music\Last Seconds Of Life.ogg")
Global EndingSFX = LoadSound("SFX\Music\Ending.ogg")
Global RadioEndingSFX = LoadSound("SFX\Radio\radioEnding.ogg")
Global LightsOnSFX = LoadSound("SFX\Events\lightsOn.ogg")

Global PDExplosionSFX = LoadSound("SFX\PDExplosion.ogg")
Global MetalSFX = LoadSound("SFX\Events\metal.ogg")
Global YouAreBraveSFX = LoadSound("SFX\Monsters\youarebrave.ogg")

Global AmbientCaveSFX = LoadSound("SFX\Events\ambient_cave.ogg")
;-----------------      SFX End      ---------------------;
;---------------------------------------------------------;


;---------------------------------------------------------;
;-----------------      GFX Start      -------------------;
Global mentalmesh = PlayerMesh
Global spider
; Monsters

Dim MentalTextures(2)
MentalTextures(0) = LoadTexture("GFX\npc\mental.jpg")
MentalTextures(1) = LoadTexture("GFX\npc\mental-2.jpg")
MentalTextures(2) = LoadTexture("GFX\npc\mental-3.jpg")
Global mental = LoadTexture("GFX\npc\mental.jpg") ; need for server
Global mental2 = LoadTexture("GFX\npc\mental-2.jpg")
Global mental3 = LoadTexture("GFX\npc\mental-3.jpg")

Dim RedmistTextures(2)
RedmistTextures(0) = LoadTexture("GFX\npc\173.jpg")
RedmistTextures(1) = LoadTexture("GFX\npc\173-2.jpg")
RedmistTextures(2) = LoadTexture("GFX\npc\173-3.jpg")
Global tex173 = LoadTexture("GFX\npc\173.jpg") ; need for server
Global tex1732 = LoadTexture("GFX\npc\173-2.jpg")

Dim EyekillerTextures(2)
EyekillerTextures(0) = LoadTexture("GFX\npc\eyekiller.jpg")
EyekillerTextures(1) = LoadTexture("GFX\npc\eyekiller-2.jpg")
EyekillerTextures(2) = LoadTexture("GFX\npc\eyekiller-3.jpg")
Global eyekiller = LoadTexture("GFX\npc\eyekiller.jpg") ; need for server

Dim UnknownTextures(2)
UnknownTextures(0) = LoadTexture("GFX\npc\unknown.jpg")
UnknownTextures(1) = LoadTexture("GFX\npc\unknown-2.jpg")
UnknownTextures(2) = LoadTexture("GFX\npc\unknown-3.jpg")

Global clothwandertex = LoadTexture("GFX\npc\clothwander.jpg") ;;TODO make/find more texture variants for clothwander
;Global unknowntex = LoadTexture("GFX\npc\unknown.jpg")

Dim GlimpseTextures(6)
GlimpseTextures(0)= LoadTexture("GFX\npc\glimpse1.png",1+2)
GlimpseTextures(1)= LoadTexture("GFX\npc\glimpse2.png",1+2)
GlimpseTextures(2)= LoadTexture("GFX\npc\glimpse3.png",1+2)
GlimpseTextures(3)= LoadTexture("GFX\npc\glimpse4.png",1+2)
GlimpseTextures(4)= LoadTexture("GFX\npc\glimpse5.png",1+2)
GlimpseTextures(5)= LoadTexture("GFX\npc\glimpse6.png",1+2)
GlimpseTextures(6)= LoadTexture("GFX\npc\glimpse7.png",1+2)

Global EnemyMental.ENEMIES
Global EnemyRedmist.ENEMIES
Global EnemyClothwander.ENEMIES
Global EnemyClothwander2.ENEMIES
Global EnemyClothwander3.ENEMIES
Global EnemyClothwander4.ENEMIES

Collisions hit_monster,hit_map, 2,3
Collisions hit_friendly,hit_map, 2,2

Collisions hit_player,hit_player, 1,3
Collisions hit_player,hit_map, 2,2 
Collisions hit_friendly,hit_monster, 1,3
Collisions hit_monster,hit_friendly, 1,3
Collisions hit_monster,hit_map, 2,2 
Collisions hit_invisible,hit_map, 2,2 
;-----------------      GFX End      ---------------------;
;---------------------------------------------------------;


;Create the vignette

	VignetteTexture = LoadTexture("GFX\fog.jpg", 1)

Vignette = CreateSprite(Camera)
	ScaleSprite(Vignette, Max(GraphicWidth / 1240.0, 1.0), Max(GraphicHeight / 960.0 * 0.8, 0.8))
	EntityTexture(Vignette, VignetteTexture)
	EntityBlend (Vignette, 2)
	EntityOrder Vignette, -1000
	MoveEntity(Vignette, 0, 0, 1.0)


;-------------------------------------------------------------------------------
;-------------------------------------------------------------------------------

;Ending settings
Global EndingFogRange# = 2.0
Global EndingFogLight = 0
Global SpeedLimit# = 1.0
Global EndingSceneMod = False


;-------------------------------------------------------------------------------
;-------------------------------------------------------------------------------

CameraFogRange(camera, 1, 6.5)
Global GameStarted = False
Global timerlowfps = 0

Global time, elapsed, ticks, tween#
Global period=1000/60
time=MilliSecs()-period

Global IsPaused = False
Global PauseTimer = 0
Global PauseBool = False
Global QUIT = True
Global GAME_END = False
dropspeed# = 0.000
Global steptimer = 0
Global breathtimer = 0
Global crouchtimer = 0
Global IsCrouched = False
Global RestartGame = False
Global MusicTimer# = 100.0
Global ZoomTimer# = 0.0
Global Sensitivity# = GetINIFloat("options.ini","options","sensitivity")
Global collider_oldy# = 0.0
Global AmbientTimer = Rand(500,2000)
Global DeepMusicON = False
Global Radio

Global DebugMode = False

ChannelVolume MusicChannel,0.00

If PlayState <> GAME_CLIENT Then
	CreateMap(flooramount)
	If area = "The Second" Then
	ElseIf PlayState = GAME_SOLO
		CreateGlimpses()
	EndIf
EndIf



If JoinToServer Then
	JoinServer(ServerAddress)
EndIf

;Intro Initialization
If introskip = False And PlayState = GAME_SOLO Then

	intro1SFX = LoadSound("SFX\intro1.ogg")
	intro2SFX = LoadSound("SFX\intro2.ogg")
	
	Dim introimg(5) 
	For i = 0 To 4
		introimg(i) = LoadImage("GFX\intro" + (i + 1) + ".jpg")
		ScaleImage(introimg(i),GraphicsWidth() / 1920.0,GraphicsHeight() / 1080.0)
	Next
EndIf
;end
	
Delay 50

Cls
tempImg = LoadImage("GFX\scpdone.jpg")
DrawImage(tempImg,(GraphicsWidth()/2)-(ImageWidth(tempImg)/2),(GraphicsHeight()/2)-(ImageHeight(tempImg)/2))
Flip
FreeImage tempImg

temp = True
While temp
	If KeyHit(57) Or PlayState <> GAME_SOLO Then temp = False
	Delay 30
Wend
Cls
Flip


;-----------------   Intro Sequence  ---------------------;
;---------------------------------------------------------;
If PlayState = GAME_SOLO And skipintro = False And area <> "The Second" And sector <> "Lower" Then

Delay 1500

;DrawImage(tempImg,(GraphicsWidth()/2)-(ImageWidth(tempImg)/2),(GraphicsHeight()/2)-(ImageHeight(tempImg)/2))
	
For i = 0 To 4
	Cls
	DrawImage(introimg(4-i),(GraphicsWidth()/2)-(ImageWidth(introimg(4-i))/2),(GraphicsHeight()/2)-(ImageHeight(introimg(4-i))/2))
	Flip
	Delay 70
Next

SoundChannel = PlaySound(intro1SFX)
While ChannelPlaying(SoundChannel)
	Delay 30
Wend

For i = 0 To 4
	Cls
	DrawImage(introimg(i),(GraphicsWidth()/2)-(ImageWidth(introimg(4-i))/2),(GraphicsHeight()/2)-(ImageHeight(introimg(4-i))/2))
	Flip
	Delay 80
Next

For i = 0 To 4
	FreeImage introimg(i)
Next
Cls
Flip

Delay 200

SoundChannel = PlaySound(intro2SFX)
While ChannelPlaying(SoundChannel)
	Delay 30
Wend

EndIf
;----------------End Of Intro Sequence--------------------;
;---------------------------------------------------------;


EntityType collider, hit_map
MoveMouse viewport_center_x, viewport_center_y
PositionEntity collider, -2.5, ColliderDefaultY#, -0.5
TurnEntity collider, 0.0, -90.0, 0.0
EntityType collider, hit_player

If PlayState = GAME_SOLO Then
	If sector = "Lower" Then
		PositionEntity collider,7.7,-98.5,-13.0
		GameStarted = True
		DeepMusicON = True
	EndIf
	
	If area = "The Second" Then
		PositionEntity collider,0.5,-280.5,-0.5
		MoveEntity collider, 0.0, 0.1, 0.0
		RotateEntity collider, 0.0, -90.0, 0.0
		GameStarted = True
		DeepMusicON = True
	EndIf
EndIf

Global introtimer = 150

PlayerFloor = (-EntityY(collider)-0.3)/2+0.1
collider_oldy = EntityY(collider)

Global CanMove = True

If PlayState = GAME_CLIENT Then
	JoinServer(ServerAddress)
EndIf

If PlayState <> GAME_SOLO Then
	GameStarted = True
EndIf

Local SensTimer% = 0
Global LoopDelay = MilliSecs()
Global ChatOpened% = False
Global message$ = ""
Global frameTimer=CreateTimer(60) 
Global AFKMode% = False

Global MatchTimer = Rand(15600,30000)

Global testlight = 100

Color 255,255,255
While QUIT
	If PLAYSTATE <> GAME_SOLO Then

		UpdateServer()

	EndIf
	
	;;;;;;DEBUG MODE;;;;;;;
	;AmbientLight 255,255,255
	;CameraRange camera, 0.001, 3000.0
	;CameraFogMode camera, 0
	;CameraFogRange camera, 1, 2000.5
	;CameraFogColor camera,0,0,0
	DebugLog "x = " + EntityX(camera)
	DebugLog "z = " + EntityZ(camera)

;-------------------------------------------------------------------------;
;-----------------      Setting up pause screen      ---------------------;
	If IntroTimer > 0 And PLAYSTATE = GAME_SOLO Then

		If introtimer = 100 Then PlaySound(FireOn)
		If introtimer <= 80 Then
			If gamestarted Then fogrange# = 4.5 Else fogrange# = 6.5
			templight1# = (81.0-introtimer)/80.0
			templight2# = max(0,templight1*24.0)
			AmbientLight (templight1#*BRIGHTNESS)+templight2#,(templight1#*BRIGHTNESS)+templight2#,(templight1#*BRIGHTNESS)+templight2#
			CameraFogRange camera, templight1#*1.0, templight1#*fogrange#
		Else
			AmbientLight 0,0,0
			CameraFogRange camera, 0.1, 0.1
		EndIf
		
		introtimer = introtimer - 1
	EndIf

	While IsPaused And PLAYSTATE = GAME_SOLO

		Cls
		RenderWorld
		UpdateBlur(0.97)
		BlurTimer = 250
		SetFont font
		Color 10,10,10
		Text (GraphicsWidth () / 2)-197,(GraphicsHeight () / 2)-115,"PAUSED"
		Color PauseTimer,PauseTimer,PauseTimer
		Text (GraphicsWidth () / 2)-194,(GraphicsHeight () / 2)-118,"PAUSED"
		
		SetFont font1
		Color 10,10,10
		Text (GraphicsWidth () / 2)-114,(GraphicsHeight () / 2)+2,"Press ESC to continue"
		Color PauseTimer,PauseTimer,PauseTimer
		Text (GraphicsWidth () / 2)-111,(GraphicsHeight () / 2),"Press ESC to continue"
		
		Color 10,10,10
		Text (GraphicsWidth () / 2)-114,(GraphicsHeight () / 2)+21,"Press SPACEBAR to quit"
		Color PauseTimer,PauseTimer,PauseTimer
		Text (GraphicsWidth () / 2)-111,(GraphicsHeight () / 2)+19,"Press SPACEBAR to quit"
		
		Color 10,10,10
		Text (GraphicsWidth () / 2)-184,(GraphicsHeight () / 2)+41,"Press +/- to change mouse sensitivity"
		Color PauseTimer,PauseTimer,PauseTimer
		Text (GraphicsWidth () / 2)-181,(GraphicsHeight () / 2)+39,"Press +/- to change mouse sensitivity"
		
		If SensTimer > 10 Then
			Color 10,10,10
			Text (GraphicsWidth () / 2)-84,(GraphicsHeight () / 2)+61,"Sensitivity: " + Int(sensitivity*100)/100.0
			Color min(PauseTimer,SensTimer),min(PauseTimer,SensTimer),min(PauseTimer,SensTimer)
			Text (GraphicsWidth () / 2)-81,(GraphicsHeight () / 2)+59,"Sensitivity: " + Int(sensitivity*100)/100.0
		EndIf
		
		Flip
		Delay 20
		If PauseTimer > 200 Then
			PauseBool = True
		ElseIf PauseTimer < 100 Then
			PauseBool = False
		EndIf
		
		If PauseBool Then
			PauseTimer = PauseTimer - 1
		Else
			PauseTimer = PauseTimer + 1
		EndIf
		
		If KeyDown(12) Then
			sensitivity = sensitivity-0.01
			SensTimer = min(250,max(150,SensTimer+2))
			If SensTimer = 250 Then sensitivity = sensitivity - 0.04	
			PutINIValue("options.ini","options","sensitivity",Int(sensitivity*100)/100.0)
		EndIf
		If KeyDown(13) Then
			sensitivity = sensitivity+0.01
			SensTimer = min(250,max(150,SensTimer+2))
			If SensTimer = 250 Then sensitivity = sensitivity + 0.04
			PutINIValue("options.ini","options","sensitivity",Int(sensitivity*100)/100.0)
		EndIf
		
		sensitivity = max(0.1,min(10,sensitivity))
		Senstimer = max(0,Senstimer - 1)
		
		If KeyHit(1) Then
			IsPaused = False
			PauseTimer = 0
			MoveMouse viewport_center_x, viewport_center_y
		EndIf
		If KeyDown(57) Then
			QUIT = False
			IsPaused = False
		EndIf
	Wend

	If KeyHit(1) Then
		If PlayState <> GAME_SOLO And AFKMode = False Then
			If IsPaused Then
				IsPaused = False
				MoveMouse viewport_center_x, viewport_center_y
			Else
				IsPaused = True
			EndIf
		ElseIf killtimer = 0 And PlayState = GAME_SOLO Then
			If FloorTimer(PlayerFloor)>1 And GameStarted Then
				SetFont font1
				PauseTimer = 150
			Else
				IsPaused = True
				SetFont font
				PauseTimer = 100
			EndIf
		EndIf
	EndIf

	
	DoRender = False

	If FrameLimit = 45 Then
		If elapsedloops Mod 4 < 3 Then DoRender = True
	ElseIf FrameLimit = 30 Then
		If elapsedloops Mod 2 = 0 Then DoRender = True
	Else
		DoRender = True
	EndIf
	
	If AFKMode = True Then DoRender = False

	elapsedloops = elapsedloops + 1
	If timer < MilliSecs() Then
		loops = elapsedloops	
		elapsedloops = 0
		timer = MilliSecs() + 1000
	EndIf
	If loops <= 45 Then
		timerlowfps = min(timerlowfps+1,500)
	Else
		timerlowfps = max(timerlowfps-1,0)
	EndIf

	If (Not ChannelPlaying (MusicChannel)) And GameStarted = True Then 
		If MUSIC_ON = True Then
			If DeepMusicON Then
				If PlayerFloor > 139
					LoopSound(MusicSecondArea)
					MusicChannel = PlaySound(MusicSecondArea)
					MusicTimer = 100
				Else
					LoopSound(MusicDeep)
					MusicChannel = PlaySound(MusicDeep)
					MusicTimer = 100
				EndIf
			Else
				MusicChannel = PlaySound(Music)
			EndIf
		EndIf
	Else
		If (Not ChannelPlaying (MusicChannel)) And GameStarted = False Then 
			MusicChannel = PlaySound(MusicIntro)
		EndIf
	EndIf

	If MusicTimer > 0 Then MusicTimer = max(MusicTimer-1,0) : ChannelVolume MusicChannel,1.0-(MusicTimer/100.0)
	
	PlayerFloor = (-EntityY(collider)-0.3)/2+0.1
	
	If FogTimer > 0 Then
		FogTimer = FogTimer - 0.4
		CameraFogRange(camera, 1, 4.2+FogTimer/100)
	EndIf
	
	Local speedMultiplier# = 1.5 * SpeedLimit
	MoveShift = False

	If KeyDown(42) And ((KeyDown(208)Or KeyDown(31)) Xor (KeyDown(200) Or  KeyDown(17)) Or (KeyDown(203) Or KeyDown(30)) Xor (KeyDown(205)Or KeyDown(32))) Then
		If StaminaTimer = 0 Then
			speedMultiplier = speedMultiplier * 2.1
			MoveShift = True
			Stamina = Stamina - 1.0
			crouchtimer = 0
		EndIf
		
		If PlayState <> GAME_SOLO And Stamina < 0 Then
			speedMultiplier = 1.5
			Stamina = 0
		EndIf
		If VisibilityTimer > 0 Then
			If Act = ACT_WALK Then
				speedMultiplier = 1.5
			EndIf
			If Act = ACT_173 Or Act = ACT_FLASH_2 Or Act = ACT_BEHIND Then
				speedMultiplier = 3.5
			EndIf
		EndIf
	EndIf

	If PLAYSTATE = GAME_SOLO Then
		If Stamina <= 0 And StaminaTimer = 0 Then
			StaminaOverload = StaminaOverload + 1
			BlurTimer= BlurTimer+2
			If (Not KeyDown(42)) Or StaminaOverload >= 140 Then
				StaminaTimer = StaminaTimer + 1
			EndIf
		EndIf
		
		If StaminaTimer > 0 Then
			state# = min(StaminaTimer*0.01,1.0)
			overload# = StaminaOverload*0.005
			speedMultiplier = speedMultiplier - (0.2 * state) - overload
			StaminaTimer = StaminaTimer + 1
			If StaminaTimer >= (370+StaminaOverload) Then StaminaTimer = 0 : StaminaOverload = max(StaminaOverload-50,0)
			If StaminaTimer = 0 And StaminaOverload > 0 Then StaminaTimer = 1
			If StaminaTimer = 30 Then PlaySound(RunBreathSFX(0))
			If speedMultiplier < 0.3 Then
				speedMultiplier = 0.0
				blurtimer = blurtimer + 0.5
				
				If crouchtimer < 30 Then
					crouchtimer = crouchtimer + 1
					PositionEntity camera, EntityX(collider), EntityY(collider)+0.58-0.01*crouchtimer, EntityZ(collider) 
					MoveEntity camera, side, up, 0 
				Else
					PositionEntity camera, EntityX(collider), EntityY(collider)+0.28, EntityZ(collider) 
					MoveEntity camera, side, up, 0 
				EndIf
			Else
				If crouchtimer > 0 Then
					crouchtimer = crouchtimer - 1
					PositionEntity camera, EntityX(collider), EntityY(collider)+0.28+0.01*(30-crouchtimer), EntityZ(collider)
					MoveEntity camera, side, up, 0 
				EndIf
			EndIf
		EndIf
	Else
		If VisibilityTimer < 0 And PlayingAs <> PLAYER_CLASSD Then
			speedMultiplier = 2.5
		EndIf
	EndIf
	
	If Act = ACT_DARKNESS And VisibilityTimer > 0 Then
		speedMultiplier = 0.7
	EndIf
	
	
	;Match Timer
	MatchTimer = MatchTimer - 1
	If MatchTimer <= 0 Then
		UpdateBlur(0.95)
		If MatchTimer = -100 Then AmbientLight 0,0,0 : PlaySound(FireOff) : BlurTimer = 200
		If MatchTimer = -290 Then PlaySound(FireOn)
		If MatchTimer = -340 Then AmbientLight Brightness,Brightness,Brightness : MatchTimer = Rand(26000, 60000)
	EndIf
			
	Stamina = min(Stamina+0.35,STAMINA_MAX)
	StaminaOverload = max(0,StaminaOverload - 0.15)
	
	If KillTimer = 0  And crouchtimer = 0 And IsCrouched = False And CanMove And ChatOpened = False And AFKMode = False Then ;Start of move
	
	If (KeyDown(208) Or KeyDown(31)) And EndingSceneMod = False  Then
		MoveEntity collider,0,0,-0.016*speedMultiplier
		MoveBack = True
	Else
		MoveBack = False
	EndIf 
	If (KeyDown(200) Or KeyDown(17)) And EndingSceneMod = False Then
		MoveEntity collider,0,0,0.02*speedMultiplier
		MoveForw = True
	Else
		MoveForw = False
	EndIf

	If (((KeyDown(208)Or KeyDown(31)) Xor KeyDown(200) Or  KeyDown(17)) Or ((KeyDown(203) Or KeyDown(30) ) Xor (KeyDown(205)Or KeyDown(32)))) And speedmultiplier > 0.0 Or EndingSceneMod Then
		
		If EndingSceneMod Then speedMultiplier = 0.25
		
		If StaminaTimer = 0 And ((Stamina < 60 And speedMultiplier > 1.5) Or Stamina < 25) Then
			If breathtimer=0 And Rand(7)=1 Then BreathChannel = PlaySound(RunBreathSFX(Rand(1,3))) : breathtimer = 53
		EndIf
		
		shake# = shake + speedMultiplier / 2
		If shakeZ >= 1.0 Then shake2coeff = -1.0
		If shakeZ <= -1.0 Then shake2coeff = 1.0
		shakeZ = shakeZ + shake2coeff*(speedMultiplier / 20.0)
		If shake >= 10.0 Then 
			shake = -10.0
			If dropspeed > -0.01 And steptimer = 0 Then
				If speedMultiplier > 1.5 Then
					EmitSound(RunSFX(Rand(0,7)), collider)
				Else
					EmitSound(StepSFX(Rand(0,7)), collider)
				EndIf
			EndIf
		EndIf
		
		If shake < 0.0 Then
 			up = up - 0.0055
		Else
			up = up + 0.0055
		EndIf
		
		If up > 0.095 Then up = 0.095
		If up < -0.005 Then up = -0.005 
		
		;----	
		shakeX# = shakeX + 0.5
		If shakeX = 20.0 Then shakeX = -20.0
		
		If shakeX < 0.0 Then
			side = side - 0.002; * speedMultiplier
			If EndingSceneMod Then side = side + 0.001
		Else
			side = side + 0.002; * speedMultiplier
			If EndingSceneMod Then side = side - 0.001
		EndIf
		;----
		
		
	EndIf 

	TurnEntity(camera,0,0,shakeZ)
	
	If (KeyDown(203) Or KeyDown(30)) And EndingSceneMod = False Then 
		MoveEntity collider,-0.012*speedMultiplier, 0, 0
		MoveLeft = True
	Else
		MoveLeft = False
	EndIf
	If (KeyDown(205) Or KeyDown(32)) And EndingSceneMod = False Then 
		MoveEntity collider,0.012*speedMultiplier, 0, 0
		MoveRight = True
	Else
		MoveRight = False
	EndIf
	
	ZoomTimer = max(ZoomTimer+0.1+1.0*40.0/max(200.0,Abs(ZoomTimer)),-400.0)
	If ZoomTimer >= 400.0 Then ZoomTimer = -400.0
	CameraZoom camera,1+(Abs(ZoomTimer/3000.0))
	
	EndIf
	
	If PlayState = GAME_SOLO Then
		UpdateEnemies()
	EndIf ;End of move
	
	DirListShift=(DirListShift+1) Mod 60
	DirList[DirListShift] = (MoveForw)+(MoveBack*2)+(MoveLeft*4)+(MoveRight*8)+(MouseHit1*16)+(MouseHit2*32)+(MoveShift*64)
	DirListAngle[DirListShift] = EntityYaw(collider,True)
	If Not isFlying Then dropspeed# = dropspeed-0.004
	If isFlying Then dropspeed# = 0.0
	MoveEntity collider,0,dropspeed,0	
	
	If PlayState <> GAME_SOLO Then UpdateWorld
	
	CollidedFloor = False
	For i = 1 To CountCollisions(collider)
		If CollisionY(collider,i) < EntityY(collider) - 0.1 Then collidedFloor = True
	Next
	
	If GameStarted = True And AmbientTimer = 0 Then
		AmbientTimer = Rand(400,1000)
		PositionEntity(SoundEmitter, EntityX(collider)+Rand(-1,1), EntityY(collider)+Rand(-1,-5), EntityZ(collider)+Rand(-1,1))
		EmitSound(AmbientSFX(Rand(0,111)),SoundEmitter)
	EndIf
	
	If GameStarted = True Then
		AmbientTimer = max(AmbientTimer-1,0)
	EndIf	
	
	If PlayState = GAME_SOLO Or PlayingAs = PLAYER_CLASSD Then
		If dropspeed# < -0.18 Then KillTimer = max(1,KillTimer) : dropspeed = 0.0
	EndIf
	
	If CollidedFloor = True Then
	If dropspeed# < -0.11 And (	PlayState = GAME_SOLO Or PlayingAs = PLAYER_CLASSD) Then KillTimer = max(1,KillTimer)
		If dropspeed# < -0.01 And steptimer = 0 And introtimer = 0 Then
			If speedMultiplier > 1.5 Then
				EmitSound(RunSFX(Rand(0,7)), collider)
				steptimer = 20
			Else
				EmitSound(StepSFX(Rand(0,7)), collider)
				steptimer = 28
				If speedMultiplier < 1.4 Then steptimer = 28
			EndIf
		EndIf
		dropspeed# = 0		
	EndIf
	
	MemX[DirListShift] = EntityX(collider,True)
	MemY[DirListShift] = EntityY(collider,True)
	MemZ[DirListShift] = EntityZ(collider,True)
	
	collider_oldy = EntityY(collider)
	steptimer = max(steptimer-1,0)
	breathtimer = max(breathtimer-1,0)
	
	If PlayState = GAME_SOLO Then
		UpdateFloors()
		UpdateGlimpses()
	EndIf

	If KillTimer>0 Then Kill()

	If FrameLimit > 0 Or FrameLimit < 255
	   	; Framelimit

		elapsed = MilliSecs()-time
		WaitingTime = (1000.0 / 60) - (MilliSecs() - LoopDelay)
		;Debug(WaitingTime)
		If WaitingTime > 0 Then
			;Delay WaitingTime
		EndIf
	  WaitTimer(FrameTimer)
	    LoopDelay = MilliSecs()
	EndIf
	If DoRender Then UpdateWorld : RenderWorld
	
	If KillTimer > 0 Then
		UpdateBlur(0.93+(KillTimer/5000.0))	
	ElseIf GameStarted = False Then
		UpdateBlur(0.7+(BlurTimer/1000.0))
		If BlurTimer = 0 Then
			UpdateBlur(0.4)
		Else
			BlurTimer = max(BlurTimer-1,0)
		EndIf
	Else
		BlurTimer = max(BlurTimer-1,0)
		If BlurTimer > 0 Then 
			UpdateBlur(0.7+(BlurTimer/1000.0))
		EndIf
		If BlurTimer = 0 Then
			UpdateBlur(0.4)	
		EndIf		
	EndIf
	SetFont MenuFont2
	If AFKMode = False Then UpdateChatMsgs()

	If PlayState <> GAME_SOLO Then
		If KeyHit(28) Then
			If ChatOpened Then
				If message$ <> "" Then
					If PlayState = GAME_CLIENT Then
						SendToServer("chat" +message$)
					Else
						AddChatMsg(PlayerName + ": " + message$,Players(0)\Red,Players(0)\Green,Players(0)\Blue,-1,True)
					EndIf
				EndIf
				message$ = ""
				ChatOpened = False
			Else
				FlushKeys()
				ChatOpened = True
			EndIf
		EndIf
		
		If ChatOpened Then
			SelectedInputBox = 1
			If Len(message$) > 12 Then
				message$ = InputBox(0, GraphicsHeight()-22, 130+(Len(message$)-12)*10, 22, message$)
			Else
				message$ = InputBox(0, GraphicsHeight()-22, 130, 22, message$)
			EndIf
			
			If Len(message$) > 100 Then message$ = Left(message$,100)
			
			;DrawImage PointerImg, MouseX(), MouseY()
		EndIf
	EndIf
	
	If PlayState = GAME_CLIENT Then
		If (MilliSecs()-Players(0)\LastMsgTime>5*1000) Then
			Color 255,0,0
			Text 5,5,"Lost connection to the server"
			Text 5,25,"Disconnecting in " + Floor((AUTO_DISCONNECT_TIME-(MilliSecs()-Players(0)\LastMsgTime))/100)/10 + "s"		
		ElseIf Client_HighPacketLose Then
			Color 255,0,0
			Text 5,5,"High packet lose detected"		
		EndIf
	EndIf
	
	If IsPaused And PLAYSTATE <> GAME_SOLO And AFKMode = False Then
		;AFK button
		buttwidth = 75
		buttheight = 25
		buttstr$ = "AFK"
		Color 30,30,30
		If(Button(width/2-buttwidth/2,height-60,buttwidth,buttheight,buttstr)) Then
			Cls
			AFKMode = Not AFKMode
			Color 255,255,255
			Text GraphicsWidth()/2,GraphicsHeight()/2-50,"You are in AFK mode.",True,True
			Text GraphicsWidth()/2,GraphicsHeight()/2-25,"The game-renderer doesn't load your computer.",True,True
			buttwidth = 75
			buttheight = 25
			buttstr$ = "AFK"
			Color 30,30,30
			If(Button(width/2-buttwidth/2,GraphicsHeight()/2,buttwidth,buttheight,buttstr)) Then
				AFKMode = Not AFKMode
			EndIf
			
			Flip
		EndIf
	EndIf
	
	If IsPaused And PLAYSTATE <> GAME_SOLO And AFKMode = False Then
		ShowPointer

		height = GraphicsHeight()
		width = GraphicsWidth()
		buttwidth = 0
		buttheight = 0
		buttstr$ = ""
		
		If SensTimer > 10 Then
			Color 10,10,10
			Text (GraphicsWidth () / 2)-84,(GraphicsHeight ())-51,"Sensitivity: " + Int(sensitivity*100)/100.0
			Color SensTimer,SensTimer,SensTimer
			Text (GraphicsWidth () / 2)-81,(GraphicsHeight ())-49,"Sensitivity: " + Int(sensitivity*100)/100.0
		EndIf
		
		If KeyDown(12) Then
			sensitivity = sensitivity-0.01
			SensTimer = min(250,max(150,SensTimer+2))
			If SensTimer = 250 Then sensitivity = sensitivity - 0.04		
			PutINIValue("options.ini","options","sensitivity",Int(sensitivity*100)/100.0)
		EndIf
		If KeyDown(13) Then
			sensitivity = sensitivity+0.01
			SensTimer = min(250,max(150,SensTimer+2))
			If SensTimer = 250 Then sensitivity = sensitivity + 0.04
			PutINIValue("options.ini","options","sensitivity",Int(sensitivity*100)/100.0)
		EndIf
		
		sensitivity = max(0.1,min(10,sensitivity))
		Senstimer = max(0,Senstimer - 1)
		
		;Quit button
		buttwidth = 100
		buttheight = 25
		buttstr$ = "Quit"
		Color 30,30,30
		If(Button(width/2-buttwidth/2,height-30,buttwidth,buttheight,buttstr)) Then
			If PlayState = GAME_CLIENT Then
				SendToServer("disconnect")
			Else
				SendToPlayers(SERVER_MESSAGE_DISCONNECT)
			EndIf
			QUIT = False
			Delay 200
		EndIf
		
		If Rights = RIGHTS_ADMIN Or Rights = RIGHTS_MONSTER Or Rights = RIGHTS_HOST Then
			addheight = 0
			If PlayerCount > 15 And ChosenButton(0) <> 2 Then
				addheight = (PlayerCount-15)*13
			EndIf
			;Admin control
			Color 0,0,0
			x = width/2-200
			y = height/2-200-addheight
			RectBox(x,y,400,400+addheight*2)
			dx = 0
			dy = 0
			
			buttwidth = 90
			buttheight = 40
			buttstr$ = "PLAYERS"
			Color 30,30,30
			If(Button(width/2+200,height/2-200 + dy,buttwidth,buttheight,buttstr,ChosenButton(0)=0)) Then
				ChosenButton(0) = 0
			EndIf
			
			If Rights = RIGHTS_ADMIN Or Rights = RIGHTS_HOST Then
				dy = dy + buttheight
				buttstr$ = "RIGHTS"
				Color 30,30,30
				If(Button(width/2+200,height/2-200 + dy,buttwidth,buttheight,buttstr,ChosenButton(0)=1)) Then
					ChosenButton(0) = 1
				EndIf
			EndIf
			
			dy = dy + buttheight
			buttstr$ = "ACTS"
			Color 30,30,30
			If(Button(width/2+200,height/2-200 + dy,buttwidth,buttheight,buttstr,ChosenButton(0)=2)) Then
				ChosenButton(0) = 2
			EndIf
			
			If ChosenButton(0) = 0 Then
				dy = 0
				
				For i = 0 To 31
					If Players(i) <> Null Then
						If Players(i)\Connected Then
							currfloor = (-EntityY(Players(i)\Pivot)-0.3)/2+0.1
							
							If i=ID Then
								currfloor = PlayerFloor
							EndIf
							
							Color 255,255,255
							playerright$ = "Class D"
							Select Players(i)\Rights
								Case RIGHTS_CLASSD:
									Color 255,255,0
									playerright = "Class D"
								Case RIGHTS_MONSTER:
									Color 255,100,100
									playerright = "Monster"
								Case RIGHTS_ADMIN:
									Color 0,0,255
									playerright = "Admin"
								Case RIGHTS_HOST:
									Color 100,255,255
									playerright = "Host"
							End Select
							
							Text width/2-180,height/2-180-addheight+dy,Players(i)\Name
							Text width/2-30,height/2-180-addheight+dy,"Floor " +currfloor
							
							If i<>ID Then
							
								buttwidth = 80
								buttheight = 25
								buttstr$ = "Kick"
								
								If Players(i)\Rights<>RIGHTS_HOST Then
									Color 30,30,30
									If(Button(width/2+105,height/2-180+dy-addheight,buttwidth,buttheight,buttstr,Players(i)\Rights=RIGHTS_ADMIN Or (Rights <> RIGHTS_HOST And Rights <> RIGHTS_ADMIN))) Then
										AddChatMsg(Players(i)\Name+ " has been kicked by " + Players(ID)\Name + ".",255,0,0,i,True)
										
										If PlayState = GAME_SERVER Then
											WriteInt Server,SERVER_MESSAGE_KICK ;kick the player
											SendUDPMsg Server,Players(i)\IP,Players(i)\Port
											FreeEntity GetChild(Players(i)\Pivot,1)
											FreeEntity Players(i)\Pivot
											Delete Players(i)
											Players(i) = Null
											PlayerCount=PlayerCount-1
										Else
											MessageSendToServer = "kick" +i
										EndIf
									EndIf
								EndIf
								
								buttwidth = 50
								buttheight = 25
								buttstr$ = "TP"
								Color 30,30,30
								If(Button(width/2+53,height/2-180+dy-addheight,buttwidth,buttheight,buttstr,VisibilityTimer > 0)) Then
									AddChatMsg("You teleported to " + Players(i)\Name + ".",100,0,100,ID,False)
									
									If PlayState = GAME_SERVER Then
										EntityType collider,hit_map
										PositionEntity(collider,EntityX(Players(i)\Pivot),EntityY(Players(i)\Pivot),EntityZ(Players(i)\Pivot))
										EntityType collider,hit_invisible
									Else
										MessageSendToServer = "teleport" +i
									EndIf
								EndIf
							EndIf
							
							dy = dy + 25
						EndIf
					EndIf
				Next
			EndIf
			
			If ChosenButton(0) = 1 Then
				dy = 0
				
				buttwidth = 80
				buttheight = 25
				
				For i = 0 To 31
					If Players(i) <> Null Then
						If Players(i)\Connected Then
							
							Color 255,255,255
							playerright$ = "Class D"
							Select Players(i)\Rights
								Case RIGHTS_CLASSD:
									Color 255,255,0
									playerright = "Class D"
								Case RIGHTS_MONSTER:
									Color 255,100,100
									playerright = "Monster"
								Case RIGHTS_ADMIN:
									Color 0,0,255
									playerright = "Admin"
								Case RIGHTS_HOST:
									Color 100,255,255
									playerright = "Host"
							End Select
							
							Text width/2-180,height/2-180+dy-addheight,Players(i)\Name
							
							If i<>ID And Players(i)\Rights<>RIGHTS_HOST Then
								Color 30,30,30
							
								buttwidth = 80
								dx = -30
								
								If Rights = RIGHTS_HOST Then
									buttstr$ = "Admin"
									If(Button(width/2+135+dx,height/2-180+dy-addheight,buttwidth,buttheight,buttstr,buttstr=playerright)) Then
										Players(i)\Rights = RIGHTS_ADMIN
										MessageSendToServer = "rights_admin" +i
									EndIf
									dx = dx - buttwidth - 2
								EndIf
								
								Color 30,30,30
								buttstr$ = "Monster"
								If(Button(width/2+135+dx,height/2-180+dy-addheight,buttwidth,buttheight,buttstr,buttstr=playerright)) Then
									Players(i)\Rights = RIGHTS_MONSTER
									MessageSendToServer = "rights_monster" +i
								EndIf
								
								Color 30,30,30
								dx = dx - buttwidth - 2
								buttstr$ = "Class D"
								If(Button(width/2+135+dx,height/2-180+dy-addheight,buttwidth,buttheight,buttstr,buttstr=playerright)) Then
									Players(i)\Rights = RIGHTS_CLASSD
									MessageSendToServer = "rights_classd" +i
									
									Players(i)\PlayingAs = PLAYER_CLASSD
									EntityTexture GetChild(Players(i)\Pivot,1),LoadTexture("GFX\npc\player.jpg")
									EntityType Players(i)\Pivot,hit_friendly
								EndIf
							EndIf
							
							dy = dy + 25
						EndIf
					EndIf
				Next
			EndIf
			
			If ChosenButton(0) = 2 Then
				dy = 20
				dx = 20
				
				
				buttwidth = 120
				buttheight = 25
				
				Color 30,30,30
				buttstr$ = "Flash"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=0 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 0
					MessageSendToServer = "act" +ACT_FLASH
					Players(ID)\Act = ACT_FLASH
					Act = ACT_FLASH
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = dx + buttwidth + 2
				buttstr$ = "Walk"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=1 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 1
					MessageSendToServer = "act" +ACT_WALK
					Players(ID)\Act = ACT_WALK
					Act = ACT_WALK
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = dx + buttwidth + 2
				buttstr$ = "Run"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=2 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 2
					MessageSendToServer = "act" +ACT_RUN
					Players(ID)\Act = ACT_RUN
					Act = ACT_RUN
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = 20
				dy = dy + buttheight + 10
				buttstr$ = "Flash 2"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=4 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 4
					MessageSendToServer = "act" +ACT_FLASH_2
					Players(ID)\Act = ACT_FLASH_2
					Act = ACT_FLASH_2
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = dx + buttwidth + 2
				buttstr$ = "DLAM"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=3 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 3
					MessageSendToServer = "act" +ACT_173
					Players(ID)\Act = ACT_173
					Act = ACT_173
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = dx + buttwidth + 2
				buttstr$ = "Behind"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=5 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 5
					MessageSendToServer = "act" +ACT_BEHIND
					Players(ID)\Act = ACT_BEHIND
					Act = ACT_BEHIND
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = 20
				dy = dy + buttheight + 10
				buttstr$ = "Blur"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=6 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 6
					MessageSendToServer = "act" +ACT_BLUR
					Players(ID)\Act = ACT_BLUR
					Act = ACT_BLUR
					UpdateLights()
				EndIf
				
				Color 30,30,30
				dx = dx + buttwidth + 2
				dx = dx + buttwidth + 2
				buttstr$ = "Darkness"
				If Button(x+dx,y+dy,buttwidth,buttheight,buttstr,ChosenButton(1)=7 Or Act=0 Or VisibilityTimer > 0) Then
					ChosenButton(1) = 7
					MessageSendToServer = "act" +ACT_DARKNESS
					Players(ID)\Act = ACT_DARKNESS
					Act = ACT_DARKNESS
					UpdateLights()
				EndIf
				
				buttwidth = 200
				buttheight = 80
				Color 30,30,30
				If Act=0 Then
					buttstr = "Become a Monster"
					ChosenButton(1) = -1
				Else
					buttstr = "Become a Class D"
				EndIf
				If Button(x+100,height/2-70,buttwidth,buttheight,buttstr,VisibilityTimer>0) Then
					If Act=0 Then
						ChosenButton(1) = 0
						MessageSendToServer = "act" +ACT_FLASH
						Act = ACT_FLASH
						Players(ID)\Act = ACT_FLASH
					Else
						ChosenButton(1) = -1
						MessageSendToServer = "act0"
						Act = 0
						Players(ID)\Act = 0
						PlayingAs = PLAYER_CLASSD%
						AmbientLight Brightness,Brightness,Brightness
					EndIf
				EndIf
				
				dx = 20
				dy = 370
				ddy = 20
				
				Color 255,255,255
				Select ChosenButton(1)
					Case 0:
						Text x+dx,y+dy-ddy*4,"Act: Flash"
						Text x+dx,y+dy-ddy*3,"Skin: Mental"
						Text x+dx,y+dy-ddy*2,"You appear for a split second"
						Text x+dx,y+dy-ddy*1,"and scare a Class D."
						PlayingAs = PLAYER_MENTAL%
					Case 1:
						Text x+dx,y+dy-ddy*4,"Act: Walk"
						Text x+dx,y+dy-ddy*3,"Skin: Mental"
						Text x+dx,y+dy-ddy*2,"You walk at normal speed and can"
						Text x+dx,y+dy-ddy*1,"kill a Class D."
						PlayingAs = PLAYER_MENTAL%
					Case 2:
						Text x+dx,y+dy-ddy*4,"Act: Run"
						Text x+dx,y+dy-ddy*3,"Skin: Mental"
						Text x+dx,y+dy-ddy*2,"You run as fast as a Class D."
						Text x+dx,y+dy-ddy*1,"Every 2 seconds the light flashes."
						PlayingAs = PLAYER_MENTAL%
					Case 3:
						Text x+dx,y+dy-ddy*5,"Act: Don't Look At Me"
						Text x+dx,y+dy-ddy*4,"Skin: Redmist"
						Text x+dx,y+dy-ddy*3,"You can't move while a Class D"
						Text x+dx,y+dy-ddy*2,"looking at you. If nobody's"
						Text x+dx,y+dy-ddy*1,"looking, you become very fast."
						PlayingAs = PLAYER_REDMIST%
					Case 4:
						Text x+dx,y+dy-ddy*4,"Act: Flash 2"
						Text x+dx,y+dy-ddy*3,"Skin: Redmist"
						Text x+dx,y+dy-ddy*2,"You appear for a second and"
						Text x+dx,y+dy-ddy*1,"run at high speed."
						PlayingAs = PLAYER_REDMIST%
					Case 5:
						Text x+dx,y+dy-ddy*5,"Act: Behind"
						Text x+dx,y+dy-ddy*4,"Skin: Redmist"
						Text x+dx,y+dy-ddy*3,"You can't move while nobody's"
						Text x+dx,y+dy-ddy*2,"looking at you. If a Class D is"
						Text x+dx,y+dy-ddy*1,"looking, you become very fast."
						PlayingAs = PLAYER_REDMIST%
					Case 6:
						Text x+dx,y+dy-ddy*4,"Act: Blur"
						Text x+dx,y+dy-ddy*3,"Skin: Eyekiller"
						Text x+dx,y+dy-ddy*2,"You blur other players' vision."
						Text x+dx,y+dy-ddy*1,"You can't move or kill."
						PlayingAs = PLAYER_EYEKILLER%
					Case 7:
						Text x+dx,y+dy-ddy*4,"Act: Darkness"
						Text x+dx,y+dy-ddy*3,"Skin: Mental"
						Text x+dx,y+dy-ddy*2,"You walk quiet slow but lights turn"
						Text x+dx,y+dy-ddy*1,"off and nobody can see you."
						PlayingAs = PLAYER_MENTAL%
				End Select
				UpdateLights()
			EndIf
			
		EndIf

		
		
	Else
		HidePointer
	EndIf
	
	If AFKMode Then
		ShowPointer

		If(Button(width/2-buttwidth/2,GraphicsHeight()/2,buttwidth,buttheight,buttstr)) Then
			AFKMode = Not AFKMode
		EndIf
	EndIf

	;TAB button to see a list of players
	If PlayState <> GAME_SOLO And KeyDown(15) Then
		dx = GraphicsWidth()/2 - 100
		dy = GraphicsHeight()/2 - PlayerCount*12.5
		
		Color 0,0,0
		RectBox(dx,dy,GraphicsWidth()-dx*2,GraphicsHeight()-dy*2)
		
		Color 255,255,255
		For i = 0 To 31
			If Players(i) <> Null Then
				If Players(i)\Connected Then
					currfloor = (-EntityY(Players(i)\Pivot)-0.3)/2+0.1
					
					If i=ID Then
						currfloor = PlayerFloor
					EndIf
					
					Select Players(i)\Rights
						Case RIGHTS_CLASSD:
							Color 255,255,0
						Case RIGHTS_MONSTER:
							Color 255,100,100
						Case RIGHTS_ADMIN:
							Color 0,0,255
						Case RIGHTS_HOST:
							Color 100,255,255
					End Select
					
					Text GraphicsWidth()/2-5-Len(Players(i)\Name)*5,dy+3,Players(i)\Name
					;Text dx+90,dy+3,currfloor
					dy = dy + 25
				EndIf
			EndIf
		Next
	EndIf

	If PauseTimer > 0 And IsPaused = False Then
		SetFont font1
		Color PauseTimer,PauseTimer,PauseTimer
		Text (GraphicsWidth () / 2)-151,(GraphicsHeight())-150,"Can't pause on this floor"
		PauseTimer = PauseTimer - 1
	EndIf

	If Not IsPaused Then 
		MouseLook()
	Else
		RotateEntity camera, user_camera_pitch, EntityYaw(collider), 0
	EndIf

	If DoRender Then
		If KillTimer = 0 Then
			If crouchtimer = 0 Then
 				PositionEntity camera, EntityX(collider), EntityY(collider)+0.58, EntityZ(collider) 
				MoveEntity camera, side, up, 0 
			EndIf
		EndIf
		Flip
	EndIf
Wend 

;---------------------------------------------------------------------------------
;---------------------------------------------------------------------------------

Function min#(n1#, n2#)
	If n1 < n2 Then Return n1 Else Return n2
End Function

Function max#(n1#, n2#)
	If n1 > n2 Then Return n1 Else Return n2
End Function

Function even(n)
	If n Mod 2 = 0 Then Return n Else Return n+1
End Function

Function CurveValue#(number#, oldn#, smooth#)
    Return oldn + (number - oldn) * (1.0 / smooth)
End Function

Function MouseLook()

	;PositionEntity camera, EntityX(collider), EntityY(collider)+1, EntityZ(collider)
	;MoveEntity camera, side, up, 0
	;MoveEntity player, side, up, 0	

	; -- Update the smoothing que to smooth the movement of the mouse.
	mouse_x_speed_5# = mouse_x_speed_4#
	mouse_x_speed_4# = mouse_x_speed_3#
	mouse_x_speed_3# = mouse_x_speed_2#
	mouse_x_speed_2# = mouse_x_speed_1#
	mouse_x_speed_1# = MouseXSpeed ()
	mouse_y_speed_5# = mouse_y_speed_4#
	mouse_y_speed_4# = mouse_y_speed_3#
	mouse_y_speed_3# = mouse_y_speed_2#
	mouse_y_speed_2# = mouse_y_speed_1#
	
	If InvertMouse Then
		mouse_y_speed_1# = -MouseYSpeed ( )	
	Else
		mouse_y_speed_1# = MouseYSpeed ( )
	EndIf
	
	the_yaw# = ( ( mouse_x_speed_1# + mouse_x_speed_2# + mouse_x_speed_3# + mouse_x_speed_4# + mouse_x_speed_5# ) / 5.0 ) * mouselook_x_inc# * sensitivity#
	the_pitch# = ( ( mouse_y_speed_1# + mouse_y_speed_2# + mouse_y_speed_3# + mouse_y_speed_4# + mouse_y_speed_5# ) / 5.0 ) * mouselook_y_inc# * sensitivity#
	;^^^^^^
	
	; -- Limit the mouse's movement. Using this method produces smoother mouselook movement than centering the mouse each loop.
	If ( MouseX() > mouse_right_limit ) Or ( MouseX() < mouse_left_limit ) Or ( MouseY() > mouse_bottom_limit ) Or ( MouseY() < mouse_top_limit )
		MoveMouse viewport_center_x, viewport_center_y
	EndIf
	;^^^^^^
	
	If EndingSceneMod Then
		If user_camera_pitch# > 30 Then user_camera_pitch# = 30.0
		If user_camera_pitch# < -50 Then user_camera_pitch# = -50.0
		RotateEntity camera, user_camera_pitch+20, EntityYaw(collider),-EntityYaw(collider)/2
		TurnEntity collider, 0.0, -the_yaw#, 0.0
		user_camera_pitch# = user_camera_pitch# + the_pitch#
		
		If EntityYaw(collider) < -50 Then RotateEntity collider,0,-50,0
		If EntityYaw(collider) > 50 Then RotateEntity collider,0,50,0
		Return
	EndIf
	
	TurnEntity collider, 0.0, -the_yaw#, 0.0 ; Turn the user on the Y (yaw) axis.
	user_camera_pitch# = user_camera_pitch# + the_pitch#
	; -- Limit the user's camera to within 180 degrees of pitch rotation. 'EntityPitch()' returns useless values so we need to use a variable to keep track of the camera pitch.
	If user_camera_pitch# > 70.0 Then user_camera_pitch# = 70.0
	If user_camera_pitch# < -70.0 Then user_camera_pitch# = -70.0
	;^^^^^^
	If KillTimer = 0 Then
		RotateEntity camera, user_camera_pitch, EntityYaw(collider),0 ; Pitch the user's camera up and down.
	EndIf
	
End Function
 
Function UpdateEnemies() 
	For enemy.ENEMIES = Each ENEMIES
		
		enemy\oldX = EntityX(enemy\collider)
		enemy\oldZ = EntityZ(enemy\collider)
		
		enemy\steptimer# = enemy\steptimer# + enemy\speed
		PositionEntity enemy\soundemitter, EntityX(enemy\collider),EntityY(enemy\collider),EntityZ(enemy\collider)
		
		If enemy\steptimer# >= 0.7 And KillTimer = 0 Then
			enemy\steptimer# = enemy\steptimer# - 0.7
			EmitSound MonsterStepSFX(Rand(0,3)),enemy\soundemitter
		EndIf
		
		PositionEntity enemy\obj, EntityX(enemy\collider), EntityY(enemy\collider) - 0.4, EntityZ(enemy\collider)	
		RotateEntity enemy\obj, 0, EntityYaw(enemy\collider), EntityRoll(enemy\collider)
		
		MoveEntity(enemy\collider, 0,1,0)
		MoveEntity(collider, 0,1,0)
		
		;If EntityVisible(enemy\collider, camera) Then
			If enemy\dorotation Then PointEntity enemy\collider, camera
			RotateEntity enemy\collider, 0, EntityYaw(enemy\collider), EntityRoll(enemy\collider)
			MoveEntity enemy\collider, 0, 0, enemy\speed
			If Distance2(EntityX(enemy\collider), EntityY(enemy\collider), EntityZ(enemy\collider)) > 0.8 Then 
				If DO_ANIMATION=True And enemy\speed > 0.00 Then Animate2(enemy\obj, AnimTime(enemy\obj), 0, 14, 10*enemy\speed)
				If DO_ANIMATION=True And enemy\speed = 0.00 Then Animate2(enemy\obj, AnimTime(enemy\obj), 205, 299, 0.08)			
			Else
				If DO_ANIMATION=True Then Animate2(enemy\obj, AnimTime(enemy\obj), 32, 44, 0.2)
			EndIf
		;EndIf 
		
		If EntityVisible(enemy\collider, camera) And BlurTimer < 160 And EndingSceneMod = False Then
			range# = Distance2(EntityX(enemy\collider),EntityY(enemy\collider),EntityZ(enemy\collider))
			BlurTimer = min(max(5.0-range#,0.0)*80,160)
			ZoomTimer = max(ZoomTimer+0.1+1.0*40.0/max(200.0,Abs(ZoomTimer)),-400.0)
		EndIf 
		
		If (Not EntityVisible(enemy\collider, camera)) And enemy\playerlastseen Then
			enemy\playerlastx# = EntityX(collider)
			enemy\playerlastz# = EntityZ(collider)
			enemy\playerlastseen = False
		ElseIf EntityVisible(enemy\collider, camera) Then
			enemy\playerlastseen = True
		EndIf
		
		MoveEntity(enemy\collider, 0,-1,0)
		MoveEntity(collider, 0,-1,0)	
		
		If EntityCollided(enemy\collider,hit_map) Then
			enemy\yspeed = 0
		Else
			enemy\yspeed = enemy\yspeed - 0.004
			TranslateEntity enemy\collider,0,enemy\yspeed,0	
		EndIf
					 
	Next
End Function 

Function CreateEnemy.ENEMIES(nx#,ny#,nz#,texture) 
	;If CurrEnemy <> Null Then
	;	FreeEntity CurrEnemy\collider
	;	FreeEntity CurrEnemy\obj
	;	Delete CurrEnemy
	;	
	;	CurrEnemy = Null
	;EndIf
	

	ny = ny + 0.2
	
	enemy.ENEMIES = New ENEMIES
	
	enemy\steptimer# = 0.0
	
	enemy\obj = LoadAnimMesh("GFX\npc\mental.b3d");CopyMesh(mentalmesh)
	ScaleEntity enemy\obj, 0.15,0.15,0.15 ;0.17
	
	enemy\collider = CreatePivot()
	PositionEntity enemy\collider, nx,ny,nz
	PositionEntity enemy\obj, nx,ny-0.05,nz	
	EntityType enemy\collider, hit_player
	EntityRadius enemy\collider, 0.2,0.5
	
	enemy\soundemitter = CreatePivot()
	ScaleEntity enemy\soundemitter, 0.6,0.6,0.6
	PositionEntity enemy\soundemitter, nx,ny,nz

	
	enemy\dorotation = True
	
	;enemy\collider2 = CreatePivot()
	;EntityRadius enemy\collider2, 2	
	
	EntityTexture enemy\obj, texture 
	
	Return enemy.ENEMIES
End Function

Function CreateFloor.FLOORS(mesh, floornum)
	room.FLOORS = New FLOORS
	room\mesh = CopyMesh(mesh)
	
	EntityPickMode room\mesh, 2
	EntityType room\mesh, hit_map
	
	If Floor(floornum/2.0)=Ceil(floornum/2.0) Then 
		PositionEntity(room\mesh, 0,-floornum*2,0)		
	Else 
		TurnEntity room\mesh, 0, 180, 0
		PositionEntity(room\mesh,8,-floornum*2,-7)
	EndIf
	
	If floornum = 0 Or floornum = 140 Then Return
	
	number$=""
		
		If floornum > 40 Then
			Select Rand(700)
				Case 1
					number = "!"
				Case 2
					number = Rand(1200,2400)
				Case 3
					number = "662"
				Case 4
					number = "LEFT"
				Case 5
					number = "D AT "
				Case 6
					number = "stop"
				Case 7
					number = "CA T ON"
				Case 8
					number = "pro l m"
				Case 9
					number = "b hin  y  "
				Case 10
					number = " "
				Case 11
					number = "ifrfk"
				Default
					number = (floornum+1)
			End Select
		Else
			number = (floornum+1)
		EndIf
		
		If (floornum > 70 And Rand(6) = 1) Or (floornum > 100 And Rand(3) = 1) Then
			number = ""
			For n = 1 To Rand(4)
				number = number + Chr(Rand(33,122))
			Next
		EndIf
		
		FloorNumberTexture(floornum)=CreateTexture(512,512) 	
		
		room\sign = CreateCube()
		ScaleEntity room\sign, 0.25,0.25,0.25
		
		Cls
		DrawImage sign, 0,0
		Color 0,0,0
		
		Text(256,256,number,True,True)
		
		CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(FloorNumberTexture(floornum)) )
		
		EntityTexture room\sign,FloorNumberTexture(floornum)
		
		If Floor(floornum/2.0)=Ceil(floornum/2.0) Then 
			PositionEntity(room\sign, -0.24,-floornum*2-0.6,-0.5)
		Else
			PositionEntity(room\sign,7.4+0.6+0.24,-floornum*2-0.6,-7+0.5)
		EndIf

End Function

Function CreateObject()
	obj.OBJECTS = New OBJECTS
	obj\mesh = CreateCube()
	
	Return obj\mesh
End Function


Function CreateGlimpse.GLIMPSES(nx#,ny#,nz#,texture)
	g.glimpses = New GLIMPSES
	g\obj= CreateSprite()
	EntityTexture g\obj, texture
	ScaleSprite g\obj, 0.3,0.3
	PositionEntity g\obj ,nx,ny,nz
End Function

Function UpdateGlimpses()
	For g.glimpses = Each GLIMPSES
		If PlayerFloor-1 > Int((-EntityY(g\obj)-0.5)/2) Then
			FreeEntity g\obj
			Delete g
		EndIf
		
		If g <> Null Then
			If PlayerFloor+1 >= Int((-EntityY(g\obj)-0.5)/2) Then 
				If Distance2(EntityX(g\obj),EntityY(collider),EntityZ(g\obj))<2.4 Or (Distance2(EntityX(g\obj),EntityY(collider),EntityZ(g\obj))<2.65 And Rand(0,15) = 1) And EntityVisible(g\obj, camera) Then
					If EntityVisible(g\obj, camera) And EntityInView(g\obj, camera) And Rand(0,2)=1 Then EmitSound(NoSFX, g\obj)
					
					FreeEntity g\obj
					Delete g
				EndIf
			EndIf
		EndIf
	Next
End Function

Function CreateGlimpses()
	Local FloorX#, FloorY#, FloorZ#
	
	If area = "The Second" Then Return
	
	For i = 8 To 100
		If (FloorActions(i) = 0) Then;And Rand(0,2)=0 Then 
			FloorX# =4
			FloorY#=-(i-1)*2-1.0
			
			If Floor(i/2.0)=Ceil(i/2.0) Then ;parillinen
				FloorZ=-6.75
				StartX = 7.5 
				EndX = 0.5
			Else ;pariton
				FloorZ=-0.25
				StartX = 0.5 
				EndX = 7.5
			EndIf
			
			CreateGlimpse(Floor(Rand(StartX,EndX))+0.5, FloorY,Floor(Rand(FloorZ,FloorZ+5.0))+0.5, GlimpseTextures(Rand(0,6)))
		EndIf 
	Next
	
	
End Function 



Function Animate2(ent, curr#, start, quit, speed#)
	SetAnimTime ent, curr + speed
	If AnimTime(ent) > quit Then SetAnimTime ent, start
	If AnimTime(ent) < start Then SetAnimTime ent, quit
End Function 

Function GetMentalRoomMesh(ent%)
	newMesh% = CreateMesh(ent)
	EntityType newMesh,hit_map2
	For i%=1 To CountSurfaces(ent)
		surf% = GetSurface(ent,i)
		brush% = GetSurfaceBrush(surf)
		If brush<>0 Then
			tex% = GetBrushTexture(brush)
			If tex<>0 Then
				If Instr(TextureName(tex),"concretefloor") Then
					newSurf = CreateSurface(newMesh)
					For j=0 To CountVertices(surf)-1
						AddVertex(newSurf,VertexX(surf,j),VertexY(surf,j),VertexZ(surf,j))
					Next
					For j=0 To CountTriangles(surf)-1
						AddTriangle(newSurf,TriangleVertex(surf,j,0),TriangleVertex(surf,j,1),TriangleVertex(surf,j,2))
					Next
				EndIf
				FreeTexture tex
			EndIf
			FreeBrush brush
		EndIf
	Next
	EntityParent newMesh,ent
	EntityAlpha newMesh,0.0
End Function

Function CreateMap(floors)
	;The First Area rooms
	map0 = LoadMesh("GFX\map0.x") 	;GFX\map0.b3d   ; First room. The player must proceed to the 6xt floor to begin
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
	map_endingScene = LoadMesh("GFX\endingScene.b3d")
	
	;The Second Area rooms
	map0_a2 = LoadMesh("GFX\map0_a2.x")	;First room
	map_a2 = LoadMesh("GFX\map_a2.x")	;Hallway
	map1_a2 = LoadMesh("GFX\map1_a2.x") ;Box room
	map2_a2 = LoadMesh("GFX\map2_a2.x") ;ACT_TRICK3
	map3_a2 = LoadMesh("GFX\map3_a2.x") ;Maze 1, ACT_ILLUSIONS
	map4_a2 = LoadMesh("GFX\map4_a2.x") ;Maze 2, ACT_ILLUSIONS_2
	map5_a2 = LoadMesh("GFX\map5_a2.x") ;Hallway with a tube
	map6_a2 = LoadMesh("GFX\map6_a2.x") ;Choice hallway
	mapending_a2 = LoadMesh("GFX\mapending_a2.x")
	
	FloorTestMode = False
	
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
	
	;
	;	Upper Sector
	;
	
	FloorActions(1)=ACT_PROCEED
	FloorTimer(1)=1
	
	temp=Rand(3,4)
	FloorActions(temp)=ACT_RADIO2
	FloorTimer(temp)=Rand(150)			
	
	temp=Rand(5,6)
	FloorActions(temp)=ACT_RADIO3
	FloorTimer(temp)=Rand(150)		
	
	FloorActions(7)=ACT_LOCK
	FloorTimer(7)=1	

	If Rand(6)=1 Then
		temp=Rand(8,9)
		FloorActions(temp)=ACT_RADIO4
		FloorTimer(temp)=Rand(150)	
	EndIf
	
	If Rand(5) = 1 Then
		temp=Rand(10,11)
		FloorActions(temp)=ACT_BREATH
		FloorTimer(temp)=1
	EndIf
	
	If Rand(5) = 1 Then
		temp=Rand(12,13)
		FloorActions(temp)=ACT_STEPS
		FloorTimer(temp)=1
	EndIf

	
	temp=Rand(15,50)
	FloorActions(temp)=ACT_LIGHT
	FloorTimer(temp)=1

	If Rand(10)<10 Then 
		temp = Rand(8,14)
		FloorActions(temp)=ACT_FLASH
		FloorTimer(temp)=Rand(1,3)
	EndIf
	
	temp = Rand(14,19)
	FloorActions(temp)=ACT_LIGHTS
	FloorTimer(temp)=1
	
	Select Rand(4)
		Case 1
			temp = Rand(20,23)
			FloorActions(temp)=ACT_TRICK1
			FloorTimer(temp)=1
		Case 2
			temp = Rand(20,23)
			FloorActions(temp)=ACT_TRICK2
			FloorTimer(temp)=1
	End Select 
	
	If Rand(5) = 1 Then
		temp = Rand(20,24)
		FloorActions(temp)=ACT_CELL
		FloorTimer(temp)=1
	EndIf
	
	If Rand(7) < 7 Then
		temp = Rand(25,30)
		FloorActions(temp)=ACT_RUN+Rand(0,3)
		FloorTimer(temp)=1
	EndIf
	
	temp = Rand(31,35)
	FloorActions(temp)=ACT_173
	FloorTimer(temp)=1
	
	temp = Rand(37,42)
	FloorActions(temp)=ACT_WAIT
	FloorTimer(temp)=1
	
	If Rand(3) = 1 Then
		temp = Rand(46,52)
		FloorActions(temp)=ACT_FLASH
		FloorTimer(temp)=1
	Else
		temp = Rand(46,52)
		FloorActions(temp)=ACT_BEHIND
		FloorTimer(temp)=1
	EndIf
	
	temp = Rand(43,47)
	FloorActions(temp)=ACT_RUN+Rand(0,3)
	FloorTimer(temp)=1
	
	;
	;	Lower Sector
	;
	
	If Rand(0,100) < 30 Then
		temp = Rand(52/2,90/2)*2
		FloorActions(temp)=ACT_SPIDER
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 30 Then
		temp = Rand(52/2,90/2)*2
		FloorActions(temp)=ACT_UNKNOWN
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 50 Then
		temp = Rand(40,90)
		FloorActions(temp)=ACT_CORNER_TRAP
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 50 Then
		temp = Rand(40,90)
		FloorActions(temp)=ACT_MENTAL_TRAP
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 40 Then
		temp = Rand(40,90)
		FloorActions(temp)=ACT_CELL_2
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 30 Then
		temp = Rand(52,90)
		FloorActions(temp)=ACT_ATTENTION
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 15 Then
		temp = Rand(52,90)
		FloorActions(temp)=ACT_TURNAROUND
		FloorTimer(temp)=1
	EndIf
	
	temp = Rand(52,90)
	FloorActions(temp)=ACT_RANDOM_SOUND
	FloorTimer(temp)=1
	temp = Rand(52,90)
	FloorActions(temp)=ACT_CHARGE173
	FloorTimer(temp)=1
	temp = Rand(52,90)
	FloorActions(temp)=ACT_RUN+Rand(0,3)
	FloorTimer(temp)=1
	temp = Rand(52,90)
	FloorActions(temp)=ACT_ROAR
	FloorTimer(temp)=1		
	temp = Rand(52,90)
	FloorActions(temp)=ACT_FLASH
	FloorTimer(temp)=1
	If Rand(0,100)<40 Then
		temp = Rand(52/2,90/2)*2
		FloorActions(temp)=ACT_BROKENROOM
		FloorTimer(temp)=1
	EndIf
	
	
	If Rand(0,100) < 50 Then
		temp = Rand(52/2,90/2)*2
		FloorActions(temp)=ACT_GORIGHT
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 70 Then
		temp = Rand(52,90)
		FloorActions(temp)=ACT_SUDDEN_ATTACK
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 90 Then
		temp = Rand(52,90)
		FloorActions(temp)=ACT_MAZE
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 80 Then
		temp = Rand(52,90)
		FloorActions(temp)=ACT_ELEVATOR
		FloorTimer(temp)=1
	EndIf
	If Rand(0,100) < 90 Then
		temp = Rand(52,90)
		FloorActions(temp)=ACT_173_2
		FloorTimer(temp)=1
	EndIf
	
	If Rand(0,100) < 80 Then
		temp = 0
		While temp = 0 Or temp Mod 2 = 1
			temp = Rand(52,80)
		Wend
		FloorActions(temp)=ACT_BLUR
		FloorTimer(temp)=1
	EndIf
	
	; Important Events
	
	FloorActions(50) = ACT_TRANSITION
	FloorTimer(50) = 1
	
	FloorActions(92) = ACT_ELEVATOR_START
	FloorTimer(92) = 1
	
	FloorActions(126) = ACT_ELEVATOR_END
	FloorTimer(126) = 1

	temp = 127
	FloorActions(temp)=ACT_ENDING
	FloorTimer(temp)=1
	FloorActions(temp+1)=ACT_NOTHING
	FloorTimer(temp+1)=2
	FloorActions(temp+2)=ACT_NOTHING
	FloorTimer(temp+2)=2
	
	temp = 51
	While temp = 0 Or temp Mod 2 = 1 Or FloorActions(temp-2) = ACT_BLUR
		temp = Rand(52,80)
	Wend
	
	FloorActions(temp)=ACT_BIGSTAIRSROOM
	FloorTimer(temp)=1
	
	;
	;	Second Area
	;
	
	temp = even(Rand(142,146))
	FloorActions(temp)=ACT_HALLWAYTRAP
	FloorTimer(temp)=1
	
	temp = even(Rand(147,150))+1
	FloorActions(temp)=ACT_ILLUSIONS+Rand(0,1)
	FloorTimer(temp)=1
	
	temp = even(Rand(151,154))+1
	FloorActions(temp)=ACT_TRICK3
	FloorTimer(temp)=1
	
	temp = even(Rand(155,159))+1
	FloorActions(temp)=ACT_ILLUSIONS+Rand(0,1)
	FloorTimer(temp)=1
	
	temp = even(162)+1
	FloorActions(temp)=ACT_ENDING_2
	FloorTimer(temp)=1
	FloorActions(temp+1)=ACT_NOTHING
	FloorTimer(temp+1)=2
	FloorActions(temp+2)=ACT_NOTHING
	FloorTimer(temp+2)=2
	floorTimer(200) = 2
	floorTimer(199) = 2
	
	SetFont font 
	
	Firstfloor = 0
	If PlayState = GAME_SOLO Then
		If sector = "Lower" Then firstfloor = 49 : FloorTimer(50) = 0
		If area = "The Second" Then Firstfloor = 140
	EndIf
	
	If firstfloor > 0 Then
		For i = 0 To firstfloor
			FloorActions(i) = 0
			FloorTimer(i) = 0
		Next
	EndIf

	For i = Firstfloor To floors-1
		If i > 135 Then Exit
		If i = 0 Then
			CreateFloor(map0,i) : MapString = MapString+ "00"
		ElseIf i < 7 And FloorTestMode = False Then
			CreateFloor(map,i) : MapString = MapString+ "99"
		Else
			Select FloorActions(i + 1)
				Case ACT_LIGHT
					If Rand(0,2)=0 Then
						CreateFloor(map2,i) : MapString = MapString+ "02"
					ElseIf Rand(0,2)=0 Then
						CreateFloor(map4,i) : MapString = MapString+ "04"
					Else
						CreateFloor(map13,i) : MapString = MapString+ "13"
					EndIf
				Case ACT_GORIGHT
					CreateFloor(map4,i) : MapString = MapString+ "04"
				Case ACT_SUDDEN_ATTACK
					CreateFloor(map3,i) : MapString = MapString+ "03"
				Case ACT_173
					CreateFloor(map2,i) : MapString = MapString+ "02"
				Case ACT_173_2
					CreateFloor(map22,i) : MapString = MapString+ "22"
				Case ACT_CELL
					CreateFloor(map1,i) : MapString = MapString+ "01"
				Case ACT_CELL_2
					CreateFloor(map14,i) : MapString = MapString+ "14"
				Case ACT_CORNER_TRAP
					CreateFloor(map2,i) : MapString = MapString+ "02"
				Case ACT_TRICK1
					CreateFloor(map5,i)	 : MapString = MapString+ "05"
				Case ACT_TRICK2
					CreateFloor(map4,i) : MapString = MapString+ "04"
				Case ACT_RUN, ACT_RUN_4, ACT_WALK, ACT_LIGHTS, ACT_TRAP, ACT_LOCK, ACT_CHARGE173
					CreateFloor(map,i) : MapString = MapString+ "99"
				Case ACT_WAIT
					CreateFloor(map11, i) : MapString = MapString+ "11"
				Case ACT_BEHIND
					CreateFloor(map13,i) : MapString = MapString+ "13"
				Case ACT_ELEVATOR
					CreateFloor(elevator,i) : MapString = MapString+ "80"
				Case ACT_BLUR
					CreateFloor(map17,i) : MapString = MapString+ "17"
				Case ACT_BROKENROOM
					CreateFloor(map20,i) : MapString = MapString+ "20"
				Case ACT_BIGSTAIRSROOM
					CreateFloor(map16,i) : MapString = MapString+ "16"
				Case ACT_173_2
					CreateFloor(map22,i) : MapString = MapString+ "22"
				Case ACT_MENTAL_TRAP
					CreateFloor(map28,i) : MapString = MapString+ "28"
				Case ACT_MAZE
					CreateFloor(map_maze,i) : MapString = MapString+ "81"
				Case ACT_ENDING
					CreateFloor(mapending,i) : MapString = MapString+ "82"
				Case ACT_NOTHING
					CreateFloor(mapnothing,i) : MapString = MapString+ "83"
				Case ACT_RUN_2, ACT_RUN_3
					CreateFloor(map26,i) : MapString = MapString+ "26"
				Case ACT_TRANSITION
					CreateFloor(map_transition,i) : MapString = MapString+ "84"
				Case ACT_ATTENTION
					CreateFloor(map12,i) : MapString = MapString+ "12"
				Case ACT_UNKNOWN
					CreateFloor(map23,i) : MapString = MapString+ "23"
				Case ACT_ELEVATOR_START
					CreateFloor(map_elevator,i) : MapString = MapString+ "80"
				Case ACT_ELEVATOR_END
					CreateFloor(map_elevator_end,i) : MapString = MapString+ "85"
				Case 0, ACT_FLASH
					Select Rand(0,30)
						Case 1,2
							CreateFloor(map,i) : MapString = MapString+ "99"
						Case 3,4
							CreateFloor(map2,i) : MapString = MapString+ "02"
						Case 5,6
							CreateFloor(map3,i) : MapString = MapString+ "03"
						Case 7
							CreateFloor(map4,i) : MapString = MapString+ "04"
						Case 8
							CreateFloor(map5,i) : MapString = MapString+ "05"
						Case 9
							CreateFloor(map6,i) : MapString = MapString+ "06"
						Case 10,11
							CreateFloor(map7,i) : MapString = MapString+ "07"
						Case 12
							If i > 50 Then 
								CreateFloor(map8,i) : MapString = MapString+ "08"
							Else 
								CreateFloor(map,i) : MapString = MapString+ "99"
							EndIf
						Case 13
							CreateFloor(map,i)  : MapString = MapString+ "99";was map9 previously
						Case 14
							CreateFloor(map,i)  : MapString = MapString+ "99";was map10 previously
						Case 15
							CreateFloor(map,i) : MapString = MapString+ "99"
						Case 16
							CreateFloor(map12,i) : MapString = MapString+ "12"
						Case 17
							CreateFloor(map14,i) : MapString = MapString+ "14"
						Case 18
							If i < 50 Then 
								CreateFloor(map15,i)  : MapString = MapString+ "15"
							Else 
								CreateFloor(map,i) : MapString = MapString+ "99"
							EndIf
						Case 19
							CreateFloor(map18,i) : MapString = MapString+ "18"
						Case 20
							If i > 50 Then 
								CreateFloor(map19,i)  : MapString = MapString+ "19"
							Else
								CreateFloor(map,i) : MapString = MapString+ "99"
							EndIf
						Case 21
							If i > 50 Then 
								CreateFloor(map21,i)  : MapString = MapString+ "21"
							Else 
								CreateFloor(map,i) : MapString = MapString+ "99"
							EndIf
						Case 22
							If i > 50 Then 
								CreateFloor(map26,i)  : MapString = MapString+ "26"
							Else 
								CreateFloor(map,i) : MapString = MapString+ "99"
							EndIf
						Case 23
							If i > 50 Then
								CreateFloor(map27,i) : MapString = MapString+ "27"
							Else
								CreateFloor(map,i) : MapString = MapString+ "99"
							EndIf
						Default
							CreateFloor(map,i) : MapString = MapString+ "99"
					End Select
				Default 
					CreateFloor(map,i) : MapString = MapString+ "99"
			End Select 
		EndIf
	Next
	
	If PlayState = GAME_SOLO Then
		For i = 140 To floors-1
			If i > 170 Then Exit
			If i = 140 Then
				CreateFloor(map0_a2,i)
			Else
				Select FloorActions(i + 1)
				
				Case ACT_ILLUSIONS
					CreateFloor(map3_a2,i)
				Case ACT_ILLUSIONS_2
					CreateFloor(map4_a2,i)
				Case ACT_HALLWAYTRAP
					CreateFloor(map_a2,i)
				Case ACT_TRICK3
					CreateFloor(map2_a2,i)
				Case ACT_ENDING_2
					CreateFloor(mapending_a2,i)
				Case ACT_NOTHING
					CreateFloor(mapnothing,i)
				Case 0
					Select Rand(0,8)
					
					Case 1
						CreateFloor(map_a2,i)
					Case 2
						CreateFloor(map1_a2,i)
					Case 3
						CreateFloor(map5_a2,i)
					Case 4
						CreateFloor(map6_a2,i)
					Default
						CreateFloor(map_a2,i)
					End Select
				End Select
			EndIf
		Next
	EndIf
	
	If PlayState <> GAME_SOLO Then
		For i = 0 To flooramount-1
			flooractions(i) = 0
			floortimer(i) = 0
		Next
	Else
		CreateFloor(map_endingScene,200)
	EndIf

	SetFont signfont

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
	FreeEntity testmap
	FreeEntity map_endingScene
	
	FreeEntity map0_a2
	FreeEntity map_a2
	FreeEntity map1_a2
	FreeEntity map2_a2
	FreeEntity map3_a2
	FreeEntity map4_a2
	FreeEntity map5_a2
	FreeEntity map6_a2
	FreeEntity mapending_a2
End Function

Function DrawFloorMarkers()
	SetFont signfont
	For i = 1 To flooramount
		number$ = ""
		
		If i > 40 Then
			Select Rand(700)
				Case 1
					number = "!"
				Case 2
					number = Rand(1200,2400)
				Case 3
					number = "665"
				Case 4
					number = "DED"
				Case 5
					number = "D AT "
				Case 6
					number = "stop"
				Case 7
					number = "CA T ON"
				Case 8
					number = "pr  l m"
				Case 9
					number = "b hin  y  "
				Case 10
					number = " "
				Case 11
					number = "NO"
				Case 12
					number = "?"
				Case 13
					number = "DO T LO K  T ME"
				Default
					number = (i + 1)
			End Select
		Else
			number = (i + 1)
		EndIf
		
		If i > 80 And Rand(3) = 1 Then
			number = ""
			For n = 1 To Rand(7)
				number = number + Chr(Rand(33,122))
			Next
		EndIf
		
		FloorNumberTexture(i) = CreateTexture(512,512) 	
		
		cube = CreateObject()
		ScaleEntity cube, 0.25,0.25,0.25
		
		Cls
		DrawImage sign, 0,0
		Color 0,0,0
		
		Text(256,256,number,True,True)
		
		CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(FloorNumberTexture(i)) )
		
		EntityTexture cube,FloorNumberTexture(i)
		
		If Floor(i/2.0)=Ceil(i/2.0) Then 
			PositionEntity(cube, -0.24,-i*2-0.6,-0.5)
		Else
			PositionEntity(cube,7.4+0.6+0.24,-i*2-0.6,-7+0.5)
		EndIf
		
		
	Next
	SetFont signfont
	
	SetBuffer BackBuffer() 	
	
End Function 


Function CheckHit()
	For enemy.ENEMIES = Each ENEMIES
		
		If EntityDistance(enemy\obj, collider) < 30 Then
			If playerweapon <> 0 Then
				If EntityCollided(Righthand, hit_head) Then End 
			Else
				If EntityCollided(RightHand, hit_head) Then End
			EndIf 
		EndIf 
			 
	Next	
End Function 

Function UpdateFloors()

	Local FloorX#, FloorY#, FloorZ#

	For i = 0 To flooramount-1
		If FloorTimer(i)>0 Then
		
			FloorX# =4
			FloorY#=-1-(i-1)*2
			
			If Floor(i/2.0)=Ceil(i/2.0) Then ;parillinen
				FloorZ=-6.75
				StartX = 7.5 
				EndX = 0.5
			Else ;pariton
				FloorZ=-0.25
				StartX = 0.5 
				EndX = 7.5
			EndIf
			
			Select FloorActions(i)
				Case ACT_ELEVATOR_START
					If Playerfloor = i And FloorTimer(i) = 1 Then
						floortimer(i) = 2
						
						brick = LoadTexture("GFX\brickwall.jpg")
						ScaleTexture brick,2,1
						door1 = CreateObject()
						PositionEntity (door1, 6.3, FloorY, -7.2)
						EntityTexture door1,brick
						EntityType door1, hit_map
						ScaleEntity door1, 0.5, 1, 0.1
						
						door2 = CreateObject()
						PositionEntity (door2, 4.3, FloorY, -7.2)
						EntityTexture door2,brick
						EntityType door2, hit_map
						ScaleEntity door2, 0.5, 1, 0.1
					EndIf
					If FloorTimer(i) = 2 And PlayerFloor = i Then
						
						If Distance2(5.4,EntityY(collider),-8.5) < 0.4 Then
							FloorTimer(i) = 3
							PlaySound(StoneSFX)
							MUSIC_ON = False
						EndIf

					EndIf
					
					If FloorTimer(i) > 2 And FloorTimer(i) < 30 Then
						MoveEntity(door1,-0.0185,0,0)
						MoveEntity(door2,0.0185,0,0)
						ChannelVolume MusicChannel,(30-FloorTimer(i))/30.0
						floortimer(i) = floorTimer(i) + 1
						
						light# = min(1.0,(35.0-FloorTimer(i))/25.0) 
						AmbientLight BRIGHTNESS*light#,BRIGHTNESS*light#,BRIGHTNESS*light#
					EndIf
					
					If FloorTimer(i) >= 30 Then
						camshake# = 0.0
						AmbientTimer = 1000
						If FloorTimer(i) = 31 Then StopChannel(MusicChannel) : PlaySound(MusicElevator)
						If FloorTimer(i) > 90 And FloorTimer(i) < 180 Then
							camshake# = 0.5
						EndIf
						If FloorTimer(i) > 179 And FloorTimer(i) < 210 Then
							camshake# = 1.3
						EndIf
						If FloorTimer(i) > 209 And FloorTimer(i) < 620 Then
							camshake# = 0.9
						EndIf
						If FloorTimer(i) > 619 And FloorTimer(i) < 1800 Then
							camshake# = 0.6
						EndIf
						
						If FloorTimer(i) = 1900 Then
							EntityType collider, hit_map
							PositionEntity collider, EntityX(collider),-250.8,EntityZ(collider)
							EntityType collider,hit_player
							MoveEntity collider, 0,-0.5,0
							brick = LoadTexture("GFX\brickwall.jpg")
							ScaleTexture brick,2,1
							
							door1 = CreateObject()
							PositionEntity (door1, 6.3, -250.9, -7.2)
							EntityTexture door1,brick
							EntityType door1, hit_map
							ScaleEntity door1, 0.5, 1, 0.1
							
							door2 = CreateObject()
							PositionEntity (door2, 4.3, -250.9, -7.2)
							EntityTexture door2,brick
							EntityType door2, hit_map
							ScaleEntity door2, 0.5, 1, 0.1
							MoveEntity(door1,-0.4995,0,0)
							MoveEntity(door2,0.4995,0,0)
							
							LoopSound(AmbientCaveSFX)
							MusicChannel = PlaySound(AmbientCaveSFX)
						EndIf
						
						If FloorTimer(i) > 1900 Then
							If FloorTimer(i) = 1901 Then PlaySound(StoneSFX)
							If FloorTimer(i) < 1960 Then
								MoveEntity(door1,0.009,0,0)
								MoveEntity(door2,-0.009,0,0)
							EndIf
							
							light# = min(1.0,(FloorTimer(i)-1860.0)/100.0)
							AmbientLight BRIGHTNESS*light#,BRIGHTNESS*light#,BRIGHTNESS*light#
							If FloorTimer(i) = 2000 Then FloorTimer(i) = 0
						EndIf
						
						TurnEntity camera,Rnd(-camshake#,camshake#),Rnd(-camshake#,camshake#),Rnd(-camshake#,camshake#)
						floortimer(i) = floorTimer(i) + 1
					EndIf
				Case ACT_ATTENTION
					If FloorTimer(i) = 1 Then
						If PlayerFloor = i Then
							If Floor(i/2.0)=Ceil(i/2.0) Then
								CurrEnemy = CreateEnemy(endx, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.013
							Else
								CurrEnemy = CreateEnemy(endx, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.013
							EndIf
							CurrEnemy\playerlastx# = EntityX(collider)
							CurrEnemy\playerlastz# = EntityZ(collider)
							CurrEnemy\dorotation = False
							Floortimer(i) = 2
						EndIf
					EndIf
					If floortimer(i)>1 Then
						If PlayerFloor > i Then
							If CurrEnemy <> Null Then
								FreeEntity CurrEnemy\collider
								FreeEntity CurrEnemy\obj
								Delete CurrEnemy
							EndIf
							FloorTimer(i) = 0
						EndIf
						If CurrEnemy <> Null Then
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 0.7 Then
								KillTimer = max(KillTimer,1)
							EndIf
							If EntityVisible(CurrEnemy\obj, camera) Then
								CurrEnemy\dorotation = True
							Else
								CurrEnemy\dorotation = False
								angle# = ATan2(-(CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)),CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider))
								RotateEntity CurrEnemy\collider,0,angle#,0
								
								dx# = CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)
								dz# = CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider)
								distance# = Sqr(dx# * dx# + dz# * dz#)
								If distance# < 0.2 Then
									CurrEnemy\playerlastx# = EntityX(collider)
									CurrEnemy\playerlastz# = EntityZ(collider)
								EndIf
							EndIf
						EndIf
					EndIf
				Case ACT_MENTAL_TRAP
					If FloorTimer(i) = 1 Then
						If PlayerFloor = i Then
							If Floor(i/2.0)=Ceil(i/2.0) Then
								CurrEnemy = CreateEnemy(0.5, FloorY-0.5, -10,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.0
							Else
								CurrEnemy = CreateEnemy(7.3, FloorY-0.5, 2.7,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.0
							EndIf
							CurrEnemy\playerlastx# = EntityX(collider)
							CurrEnemy\playerlastz# = EntityZ(collider)
							CurrEnemy\dorotation = False
							Floortimer(i) = 2
						EndIf
					EndIf
					If floortimer(i)>1 Then
						DO_ANIMATION = True
						If PlayerFloor > i Then
							If CurrEnemy <> Null Then
								FreeEntity CurrEnemy\collider
								FreeEntity CurrEnemy\obj
								Delete CurrEnemy
							EndIf
							FloorTimer(i) = 0
						EndIf
						If CurrEnemy <> Null Then
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 0.7 Then
								KillTimer = max(KillTimer,1)
							EndIf
							If EntityVisible(CurrEnemy\obj, camera) Then
								CurrEnemy\dorotation = True
								CurrEnemy\speed = 0.02
								If (floortimer(i) = 2 And distance# < Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 1.5) Then PlaySound(HorrorSFX(Rand(0, 9)))
								floortimer(i) = 3
							Else
								CurrEnemy\dorotation = False
								angle# = ATan2(-(CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)),CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider))
								RotateEntity CurrEnemy\collider,0,angle#,0
								
								dx# = CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)
								dz# = CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider)
								distance# = Sqr(dx# * dx# + dz# * dz#)
								If distance# < 0.2 Then
									CurrEnemy\playerlastx# = EntityX(collider)
									CurrEnemy\playerlastz# = EntityZ(collider)
								EndIf
							EndIf
						EndIf
					EndIf
				Case ACT_LIGHTS
					If FloorTimer(i)>1 Then
						DO_ANIMATION = True
						FloorTimer(i) = FloorTimer(i)+1
						ChannelVolume MusicChannel, 0.5
						
						If FloorTimer(i) >100 Then
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
								KillTimer = max(KillTimer,1)
							EndIf
						EndIf
						
						If FloorTimer(i) = 100 Then CurrEnemy = CreateEnemy(EndX, FloorY-0.5, FloorZ, MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.013
						If FloorTimer(i) = 210 Then PlaySound (FireOn)
						If FloorTimer(i) = 250 Then AmbientLight Brightness,Brightness,Brightness
						If FloorTimer(i) = 320  Then PlaySound(HorrorSFX(Rand(0, 9)))
						If FloorTimer(i) = 500 Then
							ChannelVolume MusicChannel, 1.0
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
							
							FloorTimer(i) = 0
						EndIf
					EndIf
				Case ACT_LIGHT
					If Distance2(FloorX, FloorY, FloorZ)<3.0 And FloorTimer(i) = 1 Then
						FloorTimer(i) = 2
					EndIf
					If FloorTimer(i)>1 Then
						FloorTimer(i) = FloorTimer(i)+1
						
						UpdateBlur(0.95)
						If FloorTimer(i) = 100 Then AmbientLight 0,0,0 : PlaySound(FireOff) : BlurTimer = 200
						If FloorTimer(i) = 290 Then PlaySound(FireOn)
						If FloorTimer(i) = 340 Then AmbientLight Brightness,Brightness,Brightness : FloorTimer(i) = 0
					EndIf
				Case ACT_RUN
					If FloorTimer(i) > 1 Then
						DO_ANIMATION = True
						FloorTimer(i) = FloorTimer(i)+1
						ChannelVolume MusicChannel, 0.5
						
						If Floor(i/2.0)=Ceil(i/2.0) Then
							first_angle = -90
						Else
							first_angle = 90
						EndIf
						
						If FloorTimer(i) >100 Then
							If Distance2(EntityX(CurrEnemy\collider), EntityY(CurrEnemy\collider), EntityZ(CurrEnemy\collider)) < 1.4 Then CurrEnemy\dorotation = True
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
								FloorTimer(i) = 451
								KillTimer = max(KillTimer,1)
								CurrEnemy\speed = 0.00
							EndIf
						EndIf
						
						If floortimer(i) = 314 Then CurrEnemy\dorotation = True
						
						If FloorTimer(i) = 130 Then
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
						If FloorTimer(i) = 100 Then 
							CurrEnemy = CreateEnemy(EndX, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							CurrEnemy\dorotation = False
							RotateEntity CurrEnemy\collider, 0,first_angle,0
							If Distance2(EntityX(CurrEnemy\collider), EntityY(CurrEnemy\collider), EntityZ(CurrEnemy\collider)) < 2.0 Then CurrEnemy\dorotation = True
						ElseIf FloorTimer(i) = 130 Or FloorTimer(i) = 260 Or FloorTimer(i) = 380 ;valot syttyy
							CameraFogRange camera, 1, 15
							AmbientLight Brightness,Brightness,Brightness
						ElseIf FloorTimer(i) = 170 Or FloorTimer(i) = 300 ; valot sammuu
							CameraFogRange camera, 1, 5.4
							AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
						ElseIf FloorTimer(i) = 450
							ChannelVolume MusicChannel, 1.0
							CameraFogRange camera, 1, 5.4
							AmbientLight Brightness,Brightness,Brightness
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
							
							FloorTimer(i) = 0	
						EndIf
					
					EndIf
				Case ACT_RUN_4
					If FloorTimer(i) > 1 Then
						DO_ANIMATION = True
						FloorTimer(i) = FloorTimer(i)+1
						
						If Floor(i/2.0)=Ceil(i/2.0) Then
							first_angle = 180
						Else
							first_angle = 0
						EndIf
						
						If FloorTimer(i) >100 Then
							If Distance2(EntityX(CurrEnemy\collider), EntityY(CurrEnemy\collider), EntityZ(CurrEnemy\collider)) < 1.4 Then CurrEnemy\dorotation = True
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
								FloorTimer(i) = 451
								KillTimer = max(KillTimer,1)
								CurrEnemy\speed = 0.00
							EndIf
						EndIf
						
						If floortimer(i) = 254 Then CurrEnemy\dorotation = True
						
						If FloorTimer(i) = 130 Then
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
						If FloorTimer(i) = 100 Then 
							If Floor(i/2.0)=Ceil(i/2.0) Then
								CurrEnemy = CreateEnemy(startx-0.3, FloorY+1.2, FloorZ+6.0,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							Else
								CurrEnemy = CreateEnemy(startx+0.3, FloorY+1.2, FloorZ-6.0,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							EndIf
							CurrEnemy\dorotation = False
							RotateEntity CurrEnemy\collider, 0,first_angle,0
							If Distance2(EntityX(CurrEnemy\collider), EntityY(CurrEnemy\collider), EntityZ(CurrEnemy\collider)) < 2.0 Then CurrEnemy\dorotation = True
						ElseIf FloorTimer(i) = 130 Or FloorTimer(i) = 260 Or FloorTimer(i) = 380 ;valot syttyy
							CameraFogRange camera, 1, 15
							AmbientLight Brightness,Brightness,Brightness
						ElseIf FloorTimer(i) = 170 Or FloorTimer(i) = 300 ; valot sammuu
							CameraFogRange camera, 1, 5.4
							AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
						ElseIf FloorTimer(i) = 450
							CameraFogRange camera, 1, 5.4
							AmbientLight Brightness,Brightness,Brightness
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
							
							FloorTimer(i) = 0	
						EndIf
					
					EndIf
				Case ACT_RUN_2
					If FloorTimer(i) > 1 Then
						DO_ANIMATION = True
						FloorTimer(i) = FloorTimer(i)+1
						
						If Floor(i/2.0)=Ceil(i/2.0) Then
							first_angle = 178
							second_angle = 90
							third_angle = 0
						Else
							first_angle = 0
							second_angle = -90
							third_angle = -180
						EndIf
						
						If FloorTimer(i) >100 Then
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
								FloorTimer(i) = 451
								KillTimer = max(KillTimer,1)
								FloorTimer(i) = FloorTimer(i) - 1
							EndIf
						EndIf
						
						If floortimer(i) = 380 Then CurrEnemy\dorotation = True
						If floortimer(i) = 240 Then RotateEntity CurrEnemy\collider, 0,second_angle,0
						If floortimer(i) = 330 Then RotateEntity CurrEnemy\collider, 0,third_angle,0
						
						If FloorTimer(i) = 130 Then
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
						If FloorTimer(i) = 100 Then 
							If Floor(i/2.0)=Ceil(i/2.0) Then
								CurrEnemy = CreateEnemy(startX-0.2, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							Else
								CurrEnemy = CreateEnemy(startX+0.2, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							EndIf
							CurrEnemy\dorotation = False
							RotateEntity CurrEnemy\collider, 0,first_angle,0
							If Distance2(EntityX(CurrEnemy\collider), EntityY(CurrEnemy\collider), EntityZ(CurrEnemy\collider)) < 2.0 Then CurrEnemy\dorotation = True
						ElseIf FloorTimer(i) = 130 Or FloorTimer(i) = 260 Or FloorTimer(i) = 380 ;valot syttyy
							CameraFogRange camera, 1, 15
							AmbientLight Brightness,Brightness,Brightness
						ElseIf FloorTimer(i) = 170 Or FloorTimer(i) = 300 ; valot sammuu
							CameraFogRange camera, 1, 5.4
							AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
						ElseIf FloorTimer(i) = 450
							CameraFogRange camera, 1, 5.4
							AmbientLight Brightness,Brightness,Brightness
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
							
							FloorTimer(i) = 0	
						EndIf
					
					EndIf
				Case ACT_RUN_3
					If FloorTimer(i) > 1 Then
						DO_ANIMATION = True
						FloorTimer(i) = FloorTimer(i)+1
						
						If Floor(i/2.0)=Ceil(i/2.0) Then
							first_angle = 180
							second_angle = -90
							third_angle = 0
						Else
							first_angle = 0
							second_angle = 90
							third_angle = -180
						EndIf
						
						If FloorTimer(i) >100 Then
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
								FloorTimer(i) = 451
								KillTimer = max(KillTimer,1)
								FloorTimer(i) = FloorTimer(i) - 1
							EndIf
						EndIf
						
						If floortimer(i) = 300 Then CurrEnemy\dorotation = True
						If floortimer(i) = 190 Then RotateEntity CurrEnemy\collider, 0,second_angle,0
						If floortimer(i) = 260 Then RotateEntity CurrEnemy\collider, 0,third_angle,0
						
						If FloorTimer(i) = 130 Then
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
						If FloorTimer(i) = 100 Then 
							If Floor(i/2.0)=Ceil(i/2.0) Then
								CurrEnemy = CreateEnemy(startX-3.2, FloorY-0.8, FloorZ-2.0,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							Else
								CurrEnemy = CreateEnemy(startX+3.2, FloorY-0.8, FloorZ+2.0,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.035
							EndIf
							CurrEnemy\dorotation = False
							RotateEntity CurrEnemy\collider, 0,first_angle,0
							If Distance2(EntityX(CurrEnemy\collider), EntityY(CurrEnemy\collider), EntityZ(CurrEnemy\collider)) < 2.0 Then CurrEnemy\dorotation = True
						ElseIf FloorTimer(i) = 130 Or FloorTimer(i) = 260 Or FloorTimer(i) = 380 ;valot syttyy
							CameraFogRange camera, 1, 15
							AmbientLight Brightness,Brightness,Brightness
						ElseIf FloorTimer(i) = 170 Or FloorTimer(i) = 300 ; valot sammuu
							CameraFogRange camera, 1, 5.4
							AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
						ElseIf FloorTimer(i) = 450
							CameraFogRange camera, 1, 5.4
							AmbientLight Brightness,Brightness,Brightness
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
							
							FloorTimer(i) = 0	
						EndIf
					
					EndIf
				Case ACT_173
					If FloorTimer(i) > 1 Then
						DO_ANIMATION = True
						FloorTimer(i) = FloorTimer(i)+1
						
						If EntityVisible(EnemyRedmist\collider, camera) And FloorTimer(i)>150 Then
							If EntityInView(EnemyRedmist\collider, camera) Then
								EnemyRedmist\speed = 0.0
								;Animate2(EnemyRedmist\obj,AnimTime(EnemyRedmist\obj),206,250,0.05)
								If FloorTimer(i)<10000 Then PlaySound(HorrorSFX(Rand(0, 9))) : FloorTimer(i) = 10001
							Else
								EnemyRedmist\speed = 0.05
							EndIf
							If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))<0.8 Then
								KillTimer = max(KillTimer,1)
									
								FloorTimer(i) = 0
							EndIf
							If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))<2.3 Then
								EnemyRedmist\speed = 0.1
							EndIf
						Else
							EnemyRedmist\speed = 0.0
						EndIf
						
						dist# = Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))
						
						If dist < 13 And PlayerFloor < (i + 1) Then
							If Not ChannelPlaying(SoundChannel) Then SoundChannel = EmitSound(DontLookSFX, EnemyRedmist\collider)
							If Not ChannelPlaying(SoundChannelReverb) Then SoundChannelReverb = EmitSound(DontlookSFXReverb, EnemyRedmist\obj)
							ChannelVolume(SoundChannel, max(0.0,25.0/(dist*dist)))
							ChannelVolume(SoundChannelReverb, max(0.2,0.01*(dist*dist)))
							If dist > 8 Then
								ChannelVolume(SoundChannelReverb, max(0.0,0.01*(dist*dist)-((dist/8.0)-1.0)))
							EndIf
						EndIf
						
						If PlayerFloor > i Then
							If EnemyRedmist <> Null Then
								FreeEntity EnemyRedmist\collider
								FreeEntity EnemyRedmist\obj
							
								Delete EnemyRedmist
							EndIf
							FloorTimer(i) = 0
						EndIf
					EndIf				
				Case ACT_173_2
					If FloorTimer(i) > 1 Then
						DO_ANIMATION = True
						
						If EntityVisible(EnemyRedmist\collider, camera) Then
							If EntityInView(EnemyRedmist\collider, camera) Then
								EnemyRedmist\speed = 0.0
								Animate2(EnemyRedmist\obj,AnimTime(EnemyRedmist\obj),206,250,0.05)
								If FloorTimer(i) = 2 Then PlaySound(HorrorSFX(Rand(0, 9))) : FloorTimer(i) = 3
							Else
								EnemyRedmist\speed = 0.05
							EndIf
							If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))<0.8 Then
								KillTimer = max(KillTimer,1)
									
								FloorTimer(i) = 0
							EndIf
							If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))<1.9 Then
								EnemyRedmist\speed = 0.1
							EndIf
						Else
							EnemyRedmist\speed = 0.0
						EndIf
						
						dist# = Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))
						
						If dist<13 And PlayerFloor < i + 1 Then
							If Not ChannelPlaying(SoundChannel) Then SoundChannel = EmitSound(DontlookSFX, EnemyRedmist\collider)
							If Not ChannelPlaying(SoundChannelReverb) Then SoundChannelReverb = EmitSound(DontlookSFXReverb, EnemyRedmist\obj)
							ChannelVolume(SoundChannel, max(0.0,25.0/(dist*dist)))
							ChannelVolume(SoundChannelReverb, max(0.2,0.01*(dist*dist)))
							If dist > 8 Then
								ChannelVolume(SoundChannelReverb, max(0.0,0.01*(dist*dist)-((dist/8.0)-1.0)))
							EndIf
						EndIf
						
						If PlayerFloor > i Then
							If EnemyRedmist <> Null Then
								FreeEntity EnemyRedmist\collider
								FreeEntity EnemyRedmist\obj
								
								Delete EnemyRedmist
							EndIf
							FloorTimer(i) = 0
						EndIf
					EndIf		
				Case ACT_BROKENROOM
					If FloorTimer(i)>1 And (PlayerFloor = i Or PlayerFloor+1 = i) Then	
						If CurrEnemy <> Null Then
							If EntityInView(CurrEnemy\obj,camera) Then
								FloorTimer(i) = FloorTimer(i) + 1
								
								If FloorTimer(i) = 10 Then
									FreeEntity CurrEnemy\collider
									FreeEntity CurrEnemy\obj
									Delete CurrEnemy
									FloorTimer(i) = 3
								EndIf
							EndIf
						EndIf
					EndIf		
				Case ACT_BIGSTAIRSROOM
					If FloorTimer(i) = 1 Then
						DO_ANIMATION = True
						door2 = CreateObject()
						If Floor(i/2.0)=Ceil(i/2.0) Then ;parillinen
							PositionEntity (door2, 0.0, FloorY+1.0, -19.0)
						Else ;pariton
							PositionEntity (door2, 8.0, FloorY, 2.0)
						EndIf
						EntityTexture door2,brickwalltexture	
						EntityType door2, hit_map
						ScaleEntity door2, 1, 1, 1
						
						btn3 = CreateObject()
						ScaleEntity(btn3, 0.1,0.1,0.1)
						If Floor(i/2.0)=Ceil(i/2.0) Then ;parillinen
							PositionEntity (btn3, -1.95, FloorY+1.0, -11.8)
						Else ;pariton
							PositionEntity (btn3, 5.2, FloorY+0.3, 7.9)
						EndIf
						EntityTexture btn3,LoadTexture("GFX\sign.jpg")
						EntityType btn3, hit_map
						FloorTimer(i) = 2
					EndIf
					If FloorTimer(i) = 2 And MouseDown(1) And Distance2(EntityX(btn3),EntityY(btn3),EntityZ(btn3))<1.0 Then
						FloorTimer(i) = 3
						EmitSound(StoneSFX, btn3)
						MoveEntity(door2, 0,1.6,0)
						CurrEnemy = CreateEnemy(4.0, FloorY+1.0, -11.0, UnknownTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.018
						PlaySound(HorrorSFX(Rand(0, 9)))
					EndIf
					If FloorTimer(i) > 2 And FloorTimer(i) < 60 Then
						MoveEntity(btn3, -0.001,0,0)
					EndIf
					If FloorTimer(i) > 2 Then
						DO_ANIMATION = True
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
							KillTimer = max(KillTimer,1)
						EndIf
						FloorTimer(i) = FloorTimer(i) + 1
					EndIf
					If Floortimer(i) = 500 Then
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj
						Delete CurrEnemy
						
						Floortimer(i) = 0
					EndIf
					
				Case ACT_TRAP
					If FloorTimer(i) > 2 Then
						DO_ANIMATION = True 
						FloorTimer(i) = FloorTimer(i) +1
						
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
							KillTimer = max(KillTimer,1)
						EndIf	
						If FloorTimer(i) = 60 Then
							FreeEntity CurrObject
							PlaySound StoneSFX
						ElseIf FloorTimer(i) = 1000
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
							FloorTimer(i) = 0
						EndIf
					EndIf
				Case ACT_WAIT
					If FloorTimer(i) > 1 Then
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.7 Then
							KillTimer = max(KillTimer,1)
						EndIf

						If EntityVisible(CurrEnemy\collider, camera) Then
							CurrEnemy\dorotation = True
						Else
							CurrEnemy\dorotation = False
							angle# = ATan2(-(CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)),CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider))
							RotateEntity CurrEnemy\collider,0,angle#,0
							
							dx# = CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)
							dz# = CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider)
							distance# = Sqr(dx# * dx# + dz# * dz#)
							If distance# < 0.1 Then
								CurrEnemy\playerlastx# = EntityX(collider)
								CurrEnemy\playerlastz# = EntityZ(collider)
							EndIf
						EndIf
					
					EndIf
					
					If FloorTimer(i) > 0 And PlayerFloor > i Then
						If CurrEnemy <> Null Then
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy
						EndIf
						FloorTimer(i) = 0
					EndIf
				
					
					If FloorTimer(i) = 2 Then
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 2.0 Then FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) +1
					EndIf
					If FloorTimer(i) = 3 Then
						PlaySound(HorrorSFX(Rand(0, 9)))
						PlaySound StoneSFX
					EndIf
					If FloorTimer(i) > 2 And FloorTimer(i) < 399 Then
						FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) +1
					EndIf
				Case ACT_SUDDEN_ATTACK
					If floorTimer(i) > 0 And PlayerFloor = i Then
						FloorTimer(i) = FloorTimer(i) + 1
						
						If FloorTimer(i) = 180 Then
							If Floor(i/2.0)=Ceil(i/2.0) Then
								CurrEnemy = CreateEnemy(7.5, FloorY-1.0, -10.5,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.013
							Else
								CurrEnemy = CreateEnemy(0.5, FloorY-1.0, 3.5,MentalTextures(Rand(0, 2))) : CurrEnemy\speed = 0.013
							EndIf
							
							PlaySound(HorrorSFX(Rand(0, 9)))
							
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) > 3.0 Then
								CurrEnemy\speed = 0.02
							EndIf
						EndIf
						
						If floorTimer(i) = 350 Then
							FloorTimer(i) = 0
							FreeEntity CurrEnemy\obj
							FreeEntity CurrEnemy\collider
							Delete CurrEnemy
						EndIf
					EndIf
					
				Case ACT_TRANSITION
					If FloorTimer(i) > 0 Then
						If Distance2(4,FloorY,-10) < 2.0 And FloorTimer(i) = 1 Then
							Radio = LoadMesh("GFX\map\radio.x")
							SoundChannel = EmitSound(radioMusic,Radio)
							ScaleEntity(Radio, 1,1,1)
							PositionEntity Radio,-1.15,FloorY-0.5,-18.8
							EntityType Radio, hit_map
							RotateEntity(Radio,0,60,0)
							DO_ANIMATION = False
							FloorTimer(i) = FloorTimer(i) + 1
							PlaySound(RoarSFX)
							
							CurrEnemy = CreateEnemy(4.5,FloorY-0.3,-12,MentalTextures(Rand(0, 2)))  
							CurrEnemy\speed = 0.00
							CurrEnemy\dorotation = False
							CurrEnemy\playerlastseen = True
						EndIf
						
						If FloorTimer(i) > 1 And FloorTimer(i) < 300 Then
							FloorTimer(i) = FloorTimer(i) + 1
							camshake# = (300-FloorTimer(i))/300.0
							TurnEntity(camera, Rnd(-camshake#,camshake#), Rnd(-camshake#,camshake#), Rnd(-camshake#,camshake#))
							Animate2(CurrEnemy\obj, AnimTime(CurrEnemy\obj), 88, 94, 0.018)	
							If Not EntityVisible(CurrEnemy\obj,camera) Then
								floortimer(i) = 300
							EndIf
						EndIf
						
						If FloorTimer(i) = 300 Then
							CurrEnemy\speed = 0.02
							CurrEnemy\dorotation = True
							DO_ANIMATION = True
							FloorTimer(i) = 400
							
							btn = CreateObject()
							ScaleEntity(btn, 0.1,0.1,0.1)
							PositionEntity (btn, 9.0, FloorY+0.1, -12.9)
							EntityTexture btn,LoadTexture("GFX\sign.jpg")
							EntityType btn, hit_map
						EndIf
						
						If FloorTimer(i) = 400 Then
							If EntityInView(btn,camera) And Distance2(EntityX(btn),EntityY(btn),EntityZ(btn)) < 0.6 And MouseDown(1) Then
								FloorTimer(i) = 401
								PlaySound StoneSFX
								PlaySound TransitionSFX
								
								FreeEntity CurrEnemy\obj
								FreeEntity CurrEnemy\collider
								Delete CurrEnemy
							EndIf
						EndIf
						
						If FloorTimer(i) > 400 And FloorTimer(i) < 460 Then
							floortimer(i) = FloorTimer(i) + 1
							MoveEntity btn,0,-0.003,0
							ChannelVolume MusicChannel,(460-FloorTimer(i))/60
						EndIf
						
						If FloorTimer(i) = 460 Then
							DeepMusicON = True
							sector = "Lower"
							PutINIValue("options.ini","options","sector",Str(sector))
							FloorTimer(i) = 0
						EndIf
						
						If FloorTimer(i) > 1 And CurrEnemy <> Null Then
							If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 0.7 Then
								KillTimer = max(1,KillTimer)
							EndIf
							If EntityVisible(CurrEnemy\collider, camera) Then
								CurrEnemy\dorotation = True
							Else
								CurrEnemy\dorotation = False
								angle# = ATan2(-(CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)),CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider))
								RotateEntity CurrEnemy\collider,0,angle#,0
								
								dx# = CurrEnemy\playerlastx# - EntityX(CurrEnemy\collider)
								dz# = CurrEnemy\playerlastz# - EntityZ(CurrEnemy\collider)
								distance# = Sqr(dx# * dx# + dz# * dz#)
								If distance# < 0.1 Then
									CurrEnemy\playerlastx# = EntityX(collider)
									CurrEnemy\playerlastz# = EntityZ(collider)
								EndIf
							EndIf
						EndIf
						
					EndIf
					
				Case ACT_ILLUSIONS
					If i = PlayerFloor And floorTimer(i) > 0 Then
						If floortimer(i) = 2 Then
							EnemyClothwander = CreateEnemy(5.4,FloorY-0.5,0.5,clothwandertex)
							EnemyClothwander2 = CreateEnemy(5.4,FloorY-0.5,8.5,clothwandertex)
							EnemyClothwander3 = CreateEnemy(-0.4,FloorY-0.5,8.5,clothwandertex)
							EnemyClothwander4 = CreateEnemy(2.4,FloorY-0.5,8.5,clothwandertex)
							
							EnemyClothwander\speed = 0.0166
							EnemyClothwander2\speed = 0.0166
							EnemyClothwander3\speed = 0.0166
							EnemyClothwander4\speed = 0.0166
							
							EnemyClothwander\steptimer# = 0
							EnemyClothwander2\steptimer# = 0.2
							EnemyClothwander3\steptimer# = 0.4
							EnemyClothwander4\steptimer# = 0.6
							
							EnemyClothwander\dorotation = False
							EnemyClothwander2\dorotation = False
							EnemyClothwander3\dorotation = False
							EnemyClothwander4\dorotation = False
						EndIf
						
						If floortimer(i) = 124 Then			
							floortimer(i) = 4
						EndIf
						
						For enemy.ENEMIES = Each ENEMIES
							If floortimer(i) = 123 Then			
								angle = EntityYaw(enemy\collider) + Rand(-1,1)*90
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							
							If EntityX(enemy\collider) > 5.5 Then
								angle = 90
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							If EntityX(enemy\collider) < -0.5 Then
								angle = 270
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							If EntityZ(enemy\collider) > 8.5 Then
								angle = 180
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							If EntityZ(enemy\collider) < 0.5 Then
								angle = 0
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							
							dist# = distance2(EntityX(enemy\collider),EntityY(enemy\collider),EntityZ(enemy\collider))
							If dist# < 0.8 Then
								KillTimer = max(1,Killtimer)
							EndIf
							If dist# < 1.4 And EntityInView(enemy\collider,camera) And ChannelPlaying(SoundChannel) = False Then
								SoundChannel = PlaySound(HorrorSFX(Rand(0, 9)))
							EndIf
						Next
						
						If floortimer(i) > 0 Then
							If Distance2(7.5,FloorY-0.5,5.5) < 2.0 Then
								For en.ENEMIES = Each ENEMIES
									FreeEntity en\collider
									FreeEntity en\obj
											
									Delete en
								Next
								floortimer(i) = -1
							EndIf
						EndIf
						
						FloorTimer(i) = FloorTimer(i)+1
					EndIf
					
				Case ACT_ILLUSIONS_2
					If i = PlayerFloor And floorTimer(i) > 0 Then
						If floortimer(i) = 2 Then
							EnemyClothwander = CreateEnemy(5.4,FloorY-0.5,0.5,clothwandertex)
							EnemyClothwander2 = CreateEnemy(5.4,FloorY-0.5,8.5,clothwandertex)
							EnemyClothwander3 = CreateEnemy(-0.4,FloorY-0.5,8.5,clothwandertex)
							EnemyClothwander4 = CreateEnemy(2.4,FloorY-0.5,8.5,clothwandertex)
							
							EnemyClothwander\speed = 0.0166
							EnemyClothwander2\speed = 0.0166
							EnemyClothwander3\speed = 0.0166
							EnemyClothwander4\speed = 0.0166
							
							EnemyClothwander\steptimer# = 0
							EnemyClothwander2\steptimer# = 0.2
							EnemyClothwander3\steptimer# = 0.4
							EnemyClothwander4\steptimer# = 0.6
							
							EnemyClothwander\dorotation = False
							EnemyClothwander2\dorotation = False
							EnemyClothwander3\dorotation = False
							EnemyClothwander4\dorotation = False
						EndIf
						
						If floortimer(i) = 124 Then			
							floortimer(i) = 4
						EndIf
						
						For enemy.ENEMIES = Each ENEMIES
							If floortimer(i) = 123 Then			
								angle = EntityYaw(enemy\collider) + Rand(-1,1)*90
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							
							If EntityX(enemy\collider) > 5.5 Then
								angle = 90
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							If EntityX(enemy\collider) < -0.5 Then
								angle = 270
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							If EntityZ(enemy\collider) > 8.5 Then
								angle = 180
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							If EntityZ(enemy\collider) < 0.5 Then
								angle = 0
								RotateEntity enemy\collider, 0,angle,0
							EndIf
							
							dist# = distance2(EntityX(enemy\collider),EntityY(enemy\collider),EntityZ(enemy\collider))
							If dist# < 0.8 Then
								KillTimer = max(1,Killtimer)
							EndIf
							If dist# < 1.4 And EntityInView(enemy\collider,camera) And ChannelPlaying(SoundChannel) = False Then
								SoundChannel = PlaySound(HorrorSFX(Rand(0, 9)))
							EndIf
						Next
						
						If floortimer(i) > 0 Then
							If Distance2(0.5,FloorY-0.5,11.0) < 2.0 Then
								For en.ENEMIES = Each ENEMIES
									FreeEntity en\collider
									FreeEntity en\obj
											
									Delete en
								Next
								floortimer(i) = -1
							EndIf
						EndIf
						
						FloorTimer(i) = FloorTimer(i)+1
					EndIf

				Case ACT_ENDING
					GAME_END = True
					If Distance2(startx+2.0, FloorY, FloorZ) < 3.0 Or FloorTimer(i) > 1 Then
						If FloorTimer(i) < 201 Then
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						DebugLog((EntityX(camera))+ " " +(EntityY(camera)-FloorY)+ " " +(EntityZ(camera)))
						If FloorTimer(i) = 2 Then ;Init
							AmbientTimer = 1000000
							platform1 = CreateObject()
							ScaleEntity(platform1, 1,0.2,1)
							PositionEntity(platform1,-7.7,FloorY-2.0,12)
							EntityTexture platform1,LoadTexture("GFX\white.jpg")
							EntityType platform1, hit_map
							
							platform2 = CreateObject()
							ScaleEntity(platform2, 0.5,2.2,0.5)
							PositionEntity(platform2,7.5,FloorY-2.9,15.5)
							EntityTexture platform2,LoadTexture("GFX\white.jpg")
							EntityType platform2, hit_map
							
							btn1 = CreateObject()
							ScaleEntity(btn1, 0.1,0.1,0.1)
							PositionEntity (btn1, 12, FloorY-2.4, 7.5)
							EntityTexture btn1,LoadTexture("GFX\sign.jpg")
							EntityType btn1, hit_map
							
							btn2 = CreateObject()
							ScaleEntity(btn2, 0.1,0.1,0.1)
							PositionEntity (btn2, -5.9, FloorY-1.0, 10)
							EntityTexture btn2,LoadTexture("GFX\sign.jpg")
							EntityType btn2, hit_map
							
							btn3 = CreateObject()
							ScaleEntity(btn3, 0.1,0.1,0.1)
							PositionEntity (btn3, -2.8, FloorY-2.0, 12.8)
							EntityTexture btn3,LoadTexture("GFX\sign.jpg")
							EntityType btn3, hit_map
							
							btn4 = CreateObject()
							ScaleEntity(btn4, 0.1,0.1,0.1)
							PositionEntity (btn4, -5.95, FloorY-0.75, 15.5)
							EntityTexture btn4,LoadTexture("GFX\sign.jpg")
							EntityType btn4, hit_map
							
							btn5 = CreateObject()
							ScaleEntity(btn5, 0.1,0.1,0.1)
							PositionEntity (btn5, 7.5, FloorY+0.3, 2.1)
							EntityTexture btn5,LoadTexture("GFX\sign.jpg")
							EntityType btn5, hit_map
		
						EndIf
						If FloorTimer(i) <= 200 Then ;Init_End
							;ChannelVolume(MusicChannel, 1.0-(FloorTimer(i)/200.0))
							CameraFogRange(camera, 1, 4.2+(FloorTimer(i)/100.0))
							MUSIC_ON = False
						EndIf
						If FloorTimer(i) =201 Then ;Button1 click
							If Distance2(EntityX(btn1), EntityY(btn1), EntityZ(btn1)) < 1.0 And MouseDown(1) And EntityInView(btn1, camera) Then
								EmitSound(StoneSFX, btn1)
								FloorTimer(i) = 202
							EndIf
						EndIf
						If FloorTimer(i) > 201 And FloorTimer(i) < 260 Then ;Button1 move
							MoveEntity(btn1,0,-0.002,0)
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						If FloorTimer(i) = 260 Then ;Button1 end of move
							DO_ANIMATION = True
							EnemyMental = CreateEnemy(13, FloorY-2.3, 8.5, MentalTextures(Rand(0, 2)))
							EnemyMental\speed = 0.008
							EnemyRedmist = CreateEnemy(0.5, FloorY-1.5, 9.5, RedmistTextures(Rand(0, 2)))
							EnemyRedmist\speed = 0.0
							EntityFX EnemyRedmist\obj,8
							PlaySound(HorrorSFX(Rand(0, 9)))
							FloorTimer(i) = 299
						EndIf
						If FloorTimer(i) = 299 Then
							If EntityInView(EnemyRedmist\collider,camera) Then
								FloorTimer(i) = 300
							EndIf
							StopChannel MusicChannel
						EndIf
						If FloorTimer(i) = 300 Then ;Button2 click
							If Distance2(EntityX(btn2), EntityY(btn2), EntityZ(btn2)) < 1.0 And MouseDown(1) And EntityInView(btn2, camera) Then
								EmitSound(StoneSFX, btn2)
								EmitSound(StoneSFX, btn3)
								FloorTimer(i) = 301
							EndIf
						EndIf
						
						If FloorTimer(i) >= 301 And FloorTimer(i) < 360 Then ;Button2 move
							MoveEntity(btn2,-0.002,0,0)
							MoveEntity(btn3,-0.004,0,0)
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						
						If FloorTimer(i) = 360 Then ;Button2 end of move
							FloorTimer(i) = 400
						EndIf
						
						If FloorTimer(i) = 400 Then ;Button3 click
							If Distance2(EntityX(btn3), EntityY(btn3), EntityZ(btn3)) < 1.0 And MouseDown(1) And EntityInView(btn3, camera) Then
								EmitSound(StoneSFX, btn3)
								FloorTimer(i) = 401
							EndIf
						EndIf
						
						If FloorTimer(i) >= 401 And FloorTimer(i) < 460 Then ;Button3 move
							MoveEntity(btn3,0.002,0,0)
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						
						If FloorTimer(i) = 460 Then ;Button3 end of move
							EmitSound(StoneSFX, platform1)
							PositionEntity(platform1,-5.7,FloorY-2.0,12)
							FloorTimer(i) = 500
						EndIf
						
						If FloorTimer(i) = 500 Then ;Button4 click
							;If Abs(EntityX(collider)-EntityX(platform2)) < 0.5 And Abs(EntityZ(collider)-EntityZ(platform2)) < 0.5 Then
							;	PositionEntity(collider,EntityX(collider),EntityY(platform2)+2.6,EntityZ(collider))
							;EndIf
							If Distance2(EntityX(btn4), EntityY(btn4), EntityZ(btn4)) < 1.0 And MouseDown(1) And EntityInView(btn4, camera) Then
								EmitSound(StoneSFX, btn4)
								FloorTimer(i) = 501
							EndIf
						EndIf
						
						If FloorTimer(i) >= 501 And FloorTimer(i) < 560 Then ;Button4 move
							MoveEntity(btn4,-0.002,0,0)
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						
						If FloorTimer(i) = 560 Then ;Button4 end of move
							EmitSound(columnmoveSFX, platform2)
							PositionEntity(platform1,-5.7,FloorY-2.0,12)
							FloorTimer(i) = 600
						EndIf
						
						If FloorTimer(i) >= 600 And FloorTimer(i) < 1800 Then ;Platform2 move down
							MoveEntity(platform2,0,-0.00174,0)
							FloorTimer(i) = FloorTimer(i) + 1
							If EntityX(collider) > 7.0 And EntityZ(collider) > 15.0 Then
								PositionEntity(collider,EntityX(collider),EntityY(platform2)+3.1,EntityZ(collider))
								dropspeed = 0.0
							EndIf
						EndIf
						If FloorTimer(i) = 1800 Then EmitSound(columnmoveSFX, platform2)
						If FloorTimer(i) >= 1800 And FloorTimer(i) < 3000 Then ;Platform2 move up
							MoveEntity(platform2,0,0.00174,0)
							If EntityX(collider) > 7.0 And EntityZ(collider) > 15.0 Then
								PositionEntity(collider,EntityX(collider),EntityY(platform2)+3.1,EntityZ(collider))
								dropspeed = 0.0
							EndIf
							FloorTimer(i) = FloorTimer(i) + 1
						EndIf
						
						If FloorTimer(i) = 3000 Then
							PositionEntity (btn4, -5.9, FloorY-0.65, 15.5)
							FloorTimer(i) = 500
						EndIf
						
						If FloorTimer(i) < 3500 And Distance2(EntityX(btn5), EntityY(btn5), EntityZ(btn5)) < 1.0 And MouseDown(1) And EntityInView(btn5, camera) Then ;Button5 click
							EnemyMental\speed = 0.0
							AmbientTimer = 2000
							FloorTimer(i) = 3500
							ChannelVolume(SoundChannel, 0.0)
							PlaySound(LightSFX)
						EndIf
						
						If FloorTimer(i) >= 3500 Then
							EntityFX EnemyRedmist\obj, 1
							CameraRange(camera, 0.001, 20)
							CameraFogRange(camera, 0, 4.8-(FloorTimer(i)-3500)/63.0)
							CameraFogColor(camera, (FloorTimer(i)-3500)/1.3, (FloorTimer(i)-3500)/1.3, (FloorTimer(i)-3500)/1.3)
							ChannelVolume(ChaseChannel, 1.0-((FloorTimer(i)-3500)/300.0))
							ChannelVolume(SoundChannel, 1.0-((FloorTimer(i)-3500)/300.0))
							ChannelVolume(SoundChannel2, 1.0-((FloorTimer(i)-3500)/300.0))
							TurnEntity(camera,Rand(-1,1),Rand(-1,1),Rand(-1,1))
							FloorTimer(i) = FloorTimer(i) + 1
						EndIf
						
						If FloorTimer(i) = 3800 Then
							
							Color 255,255,255
							CameraFogColor(camera,255,255,255)
							SetFont signfont
							For j = 0 To 30
								Cls
								UpdateWorld
								RenderWorld
								Delay 20
							Next
							FloorTimer(i) = 4000
						EndIf
						
						If FloorTimer(i) >= 4000 Then
							Clr# = max(0, 255-(FloorTimer(i)-4000.0))
							CameraFogRange(camera, 0, (FloorTimer(i)-4000.0)/63.0)
							CameraFogColor(camera, Clr#, Clr#, Clr#)
							BlurTimer = 200
							TurnEntity(camera, Rnd(-1.5,1.5), Rnd(-1.5,1.5), Rnd(-1.5,1.5))
							
							If FloorTimer(i) = 4000 Then PlaySound(Crush1SFX)
							If FloorTimer(i) = 4120 Then PlaySound(Crush2SFX)
							If FloorTimer(i) = 4240 Then PlaySound(FireOff) : AmbientLight 0,0,0 : CanMove = False
							;If FloorTimer(i) = 4400 Then PlaySound(DamageSFX)
							If FloorTimer(i) = 4980 Then 
								EntityType collider, 0
								PositionEntity collider,0.5,-280.5,-0.5
								EntityType collider, hit_player
								PlaySound(FireOn)
								GAME_END = False
							EndIf
							If FloorTimer(i) = 5020 Then 
								area = "The Second"
								PutINIValue("options.ini","options","area",Str(area))
								FreeEntity EnemyMental\collider
								FreeEntity EnemyMental\obj
								FreeEntity EnemyRedmist\collider
								FreeEntity EnemyRedmist\obj
							
								Delete EnemyMental
								Delete EnemyRedmist
								
								CanMove = True
								Blurtimer = 200
								CameraFogRange(camera, 1, 4.2)
								AmbientLight Brightness,Brightness,Brightness
								LoopSound(MusicSecondArea)
								MusicChannel = PlaySound(MusicSecondArea)
								MusicTimer = 150
								ChannelVolume MusicChannel,0.00
								FloorTimer(i) = 0
							EndIf
						EndIf
						
						
						If Floortimer(i) >= 300 And FloorTimer(i) < 3800 Then
							If Not ChannelPlaying(ChaseChannel) Then ChaseChannel = PlaySound(ChaseSFX)
							If Not ChannelPlaying(SoundChannel) Then SoundChannel = PlaySound(DontlookSFXReverb)
							If Distance2(EntityX(EnemyMental\collider),EntityY(EnemyMental\collider),EntityZ(EnemyMental\collider)) < 0.8 Then
								KillTimer = Max(KillTimer,1)
							EndIf
							If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider)) < 0.8 Then
								KillTimer = Max(KillTimer,1)
							EndIf
							If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider)) < 3.0 Then
								EnemyRedmist\speed = 0.1
							EndIf
							If (Not EntityInView(EnemyRedmist\collider,camera)) And EntityY(EnemyRedmist\collider)+0.3 > EntityY(collider) And EntityX(collider) < 8.0 Then
								EnemyRedmist\speed = 0.1
							EndIf
						EndIf
						
					EndIf
				
				Case ACT_ENDING_2
					If i <= PlayerFloor And floorTimer(i) > 0 Then
						If floortimer(i) = 1 Then
							AmbientTimer = 1000000
							EnemyClothwander = CreateEnemy(-2.0,FloorY-0.5,5.5,clothwandertex)
							EnemyClothwander2 = CreateEnemy(-2.0,FloorY-0.5,2.0,clothwandertex)
							EnemyClothwander3 = CreateEnemy(3.0,FloorY-0.5,5.5,clothwandertex)
							EnemyClothwander4 = CreateEnemy(4.5,FloorY-0.5,0.5,clothwandertex)
							
							EnemyClothwander\speed = 0.013
							EnemyClothwander2\speed = 0.013
							EnemyClothwander3\speed = 0.013
							EnemyClothwander4\speed = 0.013
							
							EnemyClothwander\dorotation = False
							EnemyClothwander2\dorotation = False
							EnemyClothwander3\dorotation = False
							EnemyClothwander4\dorotation = False
							
							door = CreateObject()
							ScaleEntity(door, 0.5,1,0.5)
							EntityType door, hit_map
							PositionEntity(door, 0.2, FloorY-4.0, 17.45)
							doortexture = LoadTexture("GFX\map\door.jpg")
							EntityTexture(door, doortexture)
							
							btn1 = CreateObject()
							ScaleEntity(btn1, 0.1,0.1,0.1)
							PositionEntity (btn1, -1.5, FloorY-1.6, 7.9)
							EntityTexture btn1,LoadTexture("GFX\sign.jpg")
							EntityType btn1, hit_map
							
							btn2 = CreateObject()
							ScaleEntity(btn2, 0.1,0.1,0.1)
							PositionEntity (btn2, 1.5, FloorY-1.6, 7.9)
							EntityTexture btn2,LoadTexture("GFX\sign.jpg")
							EntityType btn2, hit_map
							
							wall = CreateObject()
							ScaleEntity(wall, 1,1,1)
							PositionEntity (wall, 0, FloorY-4.0, 9.5)
							EntityTexture wall,brickwalltexture_a2
							EntityType wall, hit_map
							
							FloorTimer(i) = 3
						EndIf
						
						If floortimer(i) >= 3 And FloorTimer(i) < 100 Then
							ChannelVolume MusicChannel,1.0-(FloorTimer(i)/99.0)
							If floortimer(i) = 99 Then LoopSound(ChaseSFX) : MusicChannel = PlaySound(ChaseSFX) : MusicTimer = 100
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						
						If FloorTimer(i) = 100 Then ;Button1 click
							If Distance2(EntityX(btn1), EntityY(btn1), EntityZ(btn1)) < 1.0 And MouseDown(1) And EntityInView(btn1, camera) Then
								EmitSound(StoneSFX, btn1)
								FloorTimer(i) = 101
							EndIf
						EndIf
						
						If FloorTimer(i) >= 101 And FloorTimer(i) < 160 Then ;Button1 move
							MoveEntity(btn1,0.0,0,0.002)
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						
						If FloorTimer(i) = 160 Then ;Button1 end of move
							FloorTimer(i) = 200
						EndIf
						
						If FloorTimer(i) = 200 Then ;Button2 click
							If Distance2(EntityX(btn2), EntityY(btn2), EntityZ(btn2)) < 1.0 And MouseDown(1) And EntityInView(btn2, camera) Then
								EmitSound(StoneSFX, btn2)
								FloorTimer(i) = 201
							EndIf
						EndIf
						
						If FloorTimer(i) >= 201 And FloorTimer(i) < 260 Then ;Button2 move
							MoveEntity(btn2,0.0,0,0.002)
							FloorTimer(i) = FloorTimer(i)+1
						EndIf
						
						If FloorTimer(i) = 260 Then ;Button2 end of move
							PositionEntity wall,0,20,0
							PlaySound(AmbientSFX(Rand(0,111)))
							FloorTimer(i) = 300
						EndIf
						
						If FloorTimer(i) <= 300 Then
							For enemy.ENEMIES = Each ENEMIES
								If Rand(50)=1 Then
									angle = EntityYaw(enemy\collider) + Rand(-1,1)*90
									RotateEntity enemy\collider, 0,angle,0
								EndIf
								
								If EntityX(enemy\collider) > 4.5 Then
									angle = 90
									RotateEntity enemy\collider, 0,angle,0
								EndIf
								If EntityX(enemy\collider) < -3.5 Then
									angle = 270
									RotateEntity enemy\collider, 0,angle,0
								EndIf
								If EntityZ(enemy\collider) > 6.0 Then
									angle = 180
									RotateEntity enemy\collider, 0,angle,0
								EndIf
								If EntityZ(enemy\collider) < 0.5 Then
									angle = 0
									RotateEntity enemy\collider, 0,angle,0
								EndIf
								
								dist# = distance2(EntityX(enemy\collider),EntityY(enemy\collider),EntityZ(enemy\collider))
								If dist# < 0.8 Then
									KillTimer = max(1,Killtimer)
								EndIf
								If dist# < 1.4 And EntityInView(enemy\collider,camera) And ChannelPlaying(SoundChannel) = False Then
									SoundChannel = PlaySound(HorrorSFX(Rand(0, 9)))
								EndIf
							Next
						EndIf
						
						If Distance2(0,FloorY-4.0,14.0) < 1.0 And floortimer(i) = 300 Then
							MatchTimer = 100000
							floortimer(i) = 1000
							PlaySound(EscapeSFX)
							ChannelVolume MusicChannel,0.00
							EndingFogLight = 0
							EndingFogRange = 4.8
							CameraRange(camera, 0.001, 30)
							SpeedLimit = 0.5
							For en.ENEMIES = Each ENEMIES
								FreeEntity en\collider
								FreeEntity en\obj
											
								Delete en
							Next
						EndIf
						

						If FloorTimer(i) = 2020 Then
							PlaySound(LastSecondsOfLifeSFX)
							CameraZoom camera,0.5
							TeleportToFloor(200)
						EndIf
						
						If FloorTimer(i) >= 2020 And FloorTimer(i) < 2920 Then
							If floorTimer(i) Mod 4 = 0 Then EndingFogLight = max(0,EndingFogLight - 1)
							EndingFogRange = 0.15
							
							CameraFogRange(camera, 0.1, 0.15)
							CameraFogColor(camera, EndingFogLight, EndingFogLight, EndingFogLight)
							Stamina = 150
							
							FloorTimer(i) = FloorTimer(i) + 1
						EndIf
						
						If FloorTimer(i) >= 2920 And FloorTimer(i) < 5500 Then
							If FloorTimer(i) < 3000 Then EndingFogRange = EndingFogRange + 0.13
							EndingSceneMod = True
							CameraFogRange(camera, 0.1, EndingFogRange)
							CameraFogColor(camera, 70,70,70)
							Stamina = 150
							
							If floorTimer(i) = 2920 Then
								BlurTimer = 150
								DO_ANIMATION = True
								EnemyMental = CreateEnemy(EntityX(collider), EntityY(collider), EntityZ(collider)+1.2, MentalTextures(Rand(0, 2)))
								EnemyMental\speed = 0.007
								EnemyMental\dorotation = False
								RotateEntity EnemyMental\collider,0,0,0
								
								CurrEnemy = CreateEnemy(4.5, EntityY(collider), 21, UnknownTextures(Rand(0, 2)))
								CurrEnemy\dorotation = False
								CurrEnemy\speed = 0.01
								RotateEntity CurrEnemy\collider,0,180,0
							EndIf
							
							If floorTimer(i) = 3700 Then
								RotateEntity CurrEnemy\collider,0,270,0
							EndIf
							
							If floorTimer(i) = 4000 Then
								FreeEntity CurrEnemy\collider
								FreeEntity CurrEnemy\obj
								Delete CurrEnemy
							EndIf
							
							If FloorTimer(i) = 4590 Then
								BlurTimer = 300
								EnemyMental\speed = 0.0
								EnemyMental\dorotation = True
								PositionEntity EnemyMental\collider,2.35,EntityY(EnemyMental\collider)+1.5,19.2
							EndIf
							
							If floorTimer(i) = 4591 Then
								PositionEntity(collider,EntityX(collider),EntityY(collider),17.0)
							EndIf
								
							
							TurnEntity(camera,0,0,shakeZ+side*2)
							
							If FloorTimer(i) < 4590 Then
								TranslateEntity(collider,0.0,0,0.007)
							Else
								TranslateEntity(collider,0.0,0,0.0055)
								DO_ANIMATION = False
							EndIf
							
							FloorTimer(i) = FloorTimer(i) + 1
						EndIf
						
						If FloorTimer(i) >= 5500 Then
							While QUIT 
								If FloorTimer(i) = 5500 Then PlaySound(BellEndingSFX) : BlurTimer = 200
								If FloorTimer(i) = 5620 Then PlaySound(LightsOnSFX)
								If FloorTimer(i) = 5650 Then PlaySound(EndingSFX)
								If FloorTimer(i) = 5720 Then PlaySound(RadioEndingSFX)
								
								If FloorTimer(i) = 5620 Or floorTimer(i) = 5640 Or FloorTimer(i) = 5650 Then
									Cls
									DrawImage(LogoImg,(GraphicsWidth()/2)-(ImageWidth(LogoImg)/2),(GraphicsHeight()/2)-(ImageHeight(LogoImg)/2))
									Flip
								EndIf
								If floorTimer(i) = 5625 Or floorTimer(i) = 5647 Then
									Cls
									Flip
								EndIf
								
								If FloorTimer(i) < 5620 Then
									Cls
									UpdateBlur(camera)
									UpdateWorld
									RenderWorld
									Flip
								EndIf
								
								If floorTimer(i) = 8225 Or floorTimer(i) = 8247 Then
									Cls
									DrawImage(LogoImg,(GraphicsWidth()/2)-(ImageWidth(LogoImg)/2),(GraphicsHeight()/2)-(ImageHeight(LogoImg)/2))
									Flip
								EndIf
								If FloorTimer(i) = 8220 Or floorTimer(i) = 8240 Or FloorTimer(i) = 8250 Then
									Cls
									Flip
								EndIf
								
								If FloorTimer(i) > 8400 And FloorTimer(i) < 8800 Then
									Cls
									
									If FloorTimer(i) < 8600 Then
										Color FloorTimer(i)-8400,FloorTimer(i)-8400,FloorTimer(i)-8400
									Else
										Color 8800-FloorTimer(i),8800-FloorTimer(i),8800-FloorTimer(i)
									EndIf
									Text GraphicsWidth()/2,GraphicsHeight()/2,"Created by JackFastGame",True
									Flip
								EndIf
								
								If FloorTimer(i) > 8800 And FloorTimer(i) < 9200 Then
									Cls
									
									If FloorTimer(i) < 9000 Then
										Color FloorTimer(i)-8800,FloorTimer(i)-8800,FloorTimer(i)-8800
									Else
										Color 9200-FloorTimer(i),9200-FloorTimer(i),9200-FloorTimer(i)
									EndIf
									Text GraphicsWidth()/2,GraphicsHeight()/2,"Thank you for playing",True
									Flip
								EndIf
								
								If floorTimer(i) = 9260 Then QUIT = False
								
								Delay 16
								
								FloorTimer(i) = FloorTimer(i) + 1
							Wend
						EndIf
						
						If FloorTimer(i) >= 1000 And FloorTimer(i) < 2020 Then
							Stamina = 150
							MatchTimer = 100000
							SpeedLimit = max(0.0,SpeedLimit - 0.001)
							EndingFogRange = EndingFogRange - 0.006
							If FloorTimer(i) Mod 3 = 0 Then EndingFogLight = min(255,EndingFogLight + 1)
							CameraFogRange(camera, 0.1, max(0.1,EndingFogRange))
							CameraFogColor(camera, EndingFogLight, EndingFogLight, EndingFogLight)
							CameraZoom camera,max(0.5,1.0+(FloorTimer(i)-1000)/700.0)
							TurnEntity(camera,Rand(-0.3,0.3),Rand(-0.3,0.3),Rand(-0.3,0.3))
							FloorTimer(i) = FloorTimer(i) + 1
						EndIf
						
					EndIf

					
			End Select
		EndIf
	Next

		
	If FloorTimer(PlayerFloor)>0 Then
		FloorX# =4
		FloorY#=-1-(PlayerFloor-1)*2
		
		If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
			FloorZ=-6.5
			StartX = 7.5
			EndX = 0.5
		Else ;pariton
			FloorZ=-0.5
			StartX = 0.5 
			EndX = 7.5	
		EndIf
		
		Select FloorActions(PlayerFloor)
			Case ACT_PROCEED
				If FloorTimer(1) > 0 Then
					FloorTimer(1) = FloorTimer(1)+1
					ChannelVolume MusicChannel,max((FloorTimer(1)-50),0.00)/100.0
				EndIf
				If FloorTimer(1) =200 Then
					PlaySound RadioSFX(0)
					FloorTimer(1)=0
				EndIf
			Case ACT_RADIO2
				If FloorTimer(PlayerFloor)=150 Then
					PlaySound RadioSFX(1)	;signal seems to be getting weaker
					FloorTimer(PlayerFloor)=0
				ElseIf FloorTimer(PlayerFloor) > 0 Then
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
			Case ACT_RADIO3		
				If FloorTimer(PlayerFloor)=150 Then
					PlaySound RadioSFX(2)	;good luck
					FloorTimer(PlayerFloor)=0
				ElseIf FloorTimer(PlayerFloor) > 0 Then
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
			Case ACT_RADIO4		
				If FloorTimer(PlayerFloor)=150 Then
					PlaySound RadioSFX(3)	;M?RK?ILY?	
					FloorTimer(PlayerFloor)=0
					CurrEnemy = CreateEnemy(startx-0.5, FloorY-0.5, FloorZ,UnknownTextures(Rand(0, 2)))
					CurrEnemy\speed = 0.01
					CurrEnemy\dorotation = true
					RotateEntity(CurrEnemy\collider,0,180,0)
					EntityFX(CurrEnemy\obj,8)
					PlaySound(HorrorSFX(Rand(0, 9)))
				ElseIf FloorTimer(PlayerFloor) > 0 Then
					FreeEntity CurrEnemy\collider
					FreeEntity CurrEnemy\obj
					Delete CurrEnemy
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
			Case ACT_SPIDER
				If FloorTimer(PlayerFloor) = 1 And Distance2(EndX, FloorY, FloorZ)<4.5 Then
					spider = LoadAnimMesh("GFX\map\spider.x")
					FloorTimer(PlayerFloor) = 2
					PositionEntity spider,EndX,FloorY-0.95,FloorZ
					RotateEntity spider, 0,270,0
					ScaleEntity spider,0.001,0.001,0.001
					PlaySound(SpiderSFX) 
				EndIf
				If floorTimer(PlayerFloor) = 2 Then
					TranslateEntity spider,0.1,0,0
				EndIf
			Case ACT_RANDOM_SOUND
				If FloorTimer(PlayerFloor) > 0 Then
				
					If FloorTimer(PlayerFloor) = 180 Then
						If Rand(0,4) = 1 Then
							PlaySound(PDExplosionSFX)
						ElseIf Rand(0,4) = 1 Then
							PlaySound(MetalSFX)
							FloorTimer(PlayerFloor) = 1000
						ElseIf Rand(0,4) = 1 Then
							PlaySound(YouAreBraveSFX)
						Else
							PlaySound(BehindSFX)
							BlurTimer = 200
						EndIf
					EndIf
					
					If FloorTimer(PlayerFloor) >= 1000 Then
						TranslateEntity(collider, Rnd(-0.005,0.005),Rnd(-0.005,0.005),Rnd(-0.005,0.005))
						TurnEntity(camera, Rnd(-1,1), Rnd(-1,1), Rnd(-1,1))
						
						If FloorTimer(PlayerFloor) = 1080 Then FloorTimer(PlayerFloor) = -1
					ElseIf FloorTimer(PlayerFloor) = 180 Then
						FloorTimer(PlayerFloor) = -1
					EndIf
					
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
			Case ACT_FLASH ;m?rk? vilahtaa k?yt?v?n p??ss?
				DO_ANIMATION = False
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(EndX, FloorY, FloorZ)<1.5 Then
						CurrEnemy = CreateEnemy(EndX, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2)))
						PlaySound(HorrorSFX(Rand(0, 9)))
						FloorTimer(PlayerFloor) = 5
					EndIf
				ElseIf FloorTimer(PlayerFloor)= 2
					If Distance2(FloorX, FloorY, FloorZ)<1.5 Then
						CurrEnemy = CreateEnemy(FloorX, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2)))
						PlaySound(HorrorSFX(Rand(0, 9)))
						FloorTimer(PlayerFloor) = 5
					EndIf
				ElseIf FloorTimer(PlayerFloor)= 3
					If Distance2(startX, FloorY, FloorZ)<1.5 Then
						CurrEnemy = CreateEnemy(startX, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2)))
						PlaySound(HorrorSFX(Rand(0, 9)))
						FloorTimer(PlayerFloor) = 5
					EndIf	
				Else
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
					If FloorTimer(PlayerFloor) > 40 Then
					 	FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj						
						Delete CurrEnemy
						
						FloorTimer(PlayerFloor)=0
					EndIf
				EndIf
			Case ACT_TURNAROUND
				If FloorTimer(PlayerFloor) = 1 Then
					offset = 1
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then
						offset = -1
					EndIf
					If Distance2(FloorX+offset, FloorY, FloorZ)<1.5 Then
						TurnEntity collider,0,180,0
						TranslateEntity collider,offset*0.2,0.0,2*(FloorZ#-EntityZ(collider))
						FloorTimer(PlayerFloor) = 0
					EndIf
				EndIf
			Case ACT_UNKNOWN
				If FloorTimer(PlayerFloor) = 1 Then
						Radio = LoadMesh("GFX\map\radio.x")
						SoundChannel = EmitSound(radioMusic,Radio)
						ScaleEntity(Radio, 1,1,1)
						PositionEntity Radio,-1.15,FloorY-0.5,-18.8
						EntityType Radio, hit_map
						RotateEntity(Radio,0,60,0)
					If Distance2(5, FloorY, -13) < 1.0 Then
						FloorTimer(PlayerFloor) = 2
						CurrEnemy = CreateEnemy(-0.5, FloorY-0.5, -11.5,UnknownTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.15
						CurrEnemy\dorotation = False
						RotateEntity(CurrEnemy\collider,0,180,0)
						EntityFX(CurrEnemy\obj,8)
						PlaySound(HorrorSFX(Rand(0, 9)))
					EndIf
				EndIf
				If FloorTimer(PlayerFloor) > 1 Then
					DO_ANIMATION = True
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
					If FloorTimer(PlayerFloor) > 60 Then
						FloorTimer(PlayerFloor) = 0
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj						
						Delete CurrEnemy
					EndIf
				EndIf
			Case ACT_LIGHTS
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(FloorX, FloorY, FloorZ)<1.0 Then
						PlaySound(HorrorSFX(Rand(0, 9)))
						PlaySound(FireOff)
						FloorTimer(PlayerFloor) = 2
						AmbientLight Max(BRIGHTNESS*0.25,0),Max(BRIGHTNESS*0.25,0),Max(BRIGHTNESS*0.25,0)
					EndIf
				EndIf
			Case ACT_STEPS
				If FloorTimer(PlayerFloor)= 1 Then
					FloorTimer(PlayerFloor)= 2
				ElseIf FloorTimer(PlayerFloor)< 3000
					If Distance2(EndX, FloorY, FloorZ)<6 Then 
						PositionEntity SoundEmitter,FloorX+(FloorX-EndX)*1.1,FloorY,FloorZ
						FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
						If FloorTimer(PlayerFloor) Mod 150 < Rand(50) Then
							EmitSound(loudstepsound, SoundEmitter)
							FloorTimer(PlayerFloor) = 51
						EndIf
					EndIf
				EndIf		
			Case ACT_BREATH  
				If FloorTimer( PlayerFloor)= 1 Then
					FloorTimer(PlayerFloor)= 2
				ElseIf FloorTimer(PlayerFloor)< 3000
						Radio = LoadMesh("GFX\map\radio.x")
						SoundChannel = EmitSound(radioMusic,Radio)
						ScaleEntity(Radio, 1,1,1)
						PositionEntity Radio,-1.15,FloorY-0.5,-18.8
						EntityType Radio, hit_map
						RotateEntity(Radio,0,60,0)
					If Distance2(EndX, FloorY, FloorZ)<7 Then 
						PositionEntity SoundEmitter,FloorX+(FloorX-EndX)*1.1,FloorY,FloorZ
						FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
						If (FloorTimer(PlayerFloor) Mod 600) < 10 Then
							EmitSound(BreathSFX, SoundEmitter)
							FloorTimer(PlayerFloor) = 11
						EndIf
					EndIf
				EndIf	
			Case ACT_RUN
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(FloorX-1.0, FloorY, FloorZ)<1.0 Then
						PlaySound(HorrorSFX(Rand(0, 9)))
						PlaySound(FireOff)
						FloorTimer(PlayerFloor) = 2
						AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
					EndIf
				EndIf
			Case ACT_RUN_4
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(FloorX-1.0, FloorY, FloorZ)<1.0 Then
						PlaySound(HorrorSFX(Rand(0, 9)))
						PlaySound(FireOff)
						FloorTimer(PlayerFloor) = 2
						AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
					EndIf
				EndIf
			Case ACT_RUN_2
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(startX, FloorY, FloorZ-5.0)<1.0 Or Distance2(startX, FloorY, FloorZ+5.0)<1.0 Then
						PlaySound(HorrorSFX(Rand(0, 9)))
						PlaySound(FireOff)
						FloorTimer(PlayerFloor) = 2
						AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
					EndIf
				EndIf
			Case ACT_RUN_3
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(startX, FloorY, FloorZ-5.0)<1.0 Or Distance2(startX, FloorY, FloorZ+5.0)<1.0 Then
						PlaySound(HorrorSFX(Rand(0, 9)))
						PlaySound(FireOff)
						FloorTimer(PlayerFloor) = 2
						AmbientLight Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10),Max(BRIGHTNESS-50,10)
					EndIf
				EndIf
			Case ACT_173
				If FloorTimer(PlayerFloor)= 1 Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						EnemyRedmist = CreateEnemy(startx-1.8,FloorY-0.5,FloorZ-6.0,tex1732)  
					Else ;pariton
						EnemyRedmist = CreateEnemy(startx+1.8,FloorY-0.5,FloorZ+6.0,RedmistTextures(Rand(0, 2)))  
					EndIf
					EntityFX EnemyRedmist\obj, 8
					EnemyRedmist\speed = 0.0
					FloorTimer(PlayerFloor) = 2
				EndIf
			Case ACT_173_2
				If FloorTimer(PlayerFloor)= 1 Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						EnemyRedmist = CreateEnemy(2.5,FloorY-0.5,-12.5,RedmistTextures(Rand(0, 2)))	
					Else ;pariton
						EnemyRedmist = CreateEnemy(5.5,FloorY-0.5,5.55,tex1732)   
					EndIf
					EntityFX EnemyRedmist\obj, 8
					EnemyRedmist\speed = 0.0
					
					door = CreateObject()
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						PositionEntity (door, 0.0, FloorY, -9.0)
					Else ;pariton
						PositionEntity (door, 8.0, FloorY, 2.0)
					EndIf
					EntityTexture door,brickwalltexture	
					EntityType door, hit_map
					ScaleEntity door, 1, 1, 1
					
					btn1 = CreateObject()
					ScaleEntity(btn1, 0.1,0.1,0.1)
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						PositionEntity (btn1, 2.5, FloorY+0.3, -14.9)
					Else ;pariton
						PositionEntity (btn1, 5.2, FloorY+0.3, 7.9)
					EndIf
					EntityTexture btn1,LoadTexture("GFX\sign.jpg")
					EntityType btn1, hit_map
					
					FloorTimer(PlayerFloor) = 2
				EndIf
				
				If FloorTimer(PlayerFloor) = 3 Then ;Button1 click
					;DebugLog(Distance2(EntityX(btn1), EntityY(btn1), EntityZ(btn1)))
					If Distance2(EntityX(btn1), EntityY(btn1), EntityZ(btn1)) < 1.6 And MouseDown(1) And EntityInView(btn1, camera) Then
						EmitSound(StoneSFX, btn1)
						FloorTimer(PlayerFloor) = 4
					EndIf
				EndIf
				
				If FloorTimer(PlayerFloor) > 3 And FloorTimer(PlayerFloor) < 62 Then ;Button1 move
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then
						MoveEntity(btn1,0,0,-0.002)
					Else
						MoveEntity(btn1,0,0,0.002)
					EndIf
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
				EndIf
				
				If FloorTimer(PlayerFloor) = 62 Then ;Button1 end of move
					EmitSound(AmbientSFX(Rand(0,111)), door)
					EmitSound(AmbientSFX(Rand(0,111)), door)
					FloorTimer(PlayerFloor) = 100
				EndIf
				
				If FloorTimer(PlayerFloor) >= 100 And FloorTimer(PlayerFloor) <= 200 Then
					MoveEntity(door,0,0.016,0)
					Floortimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
			
			Case ACT_GORIGHT
				DO_ANIMATION = True
				If FloorTimer(PlayerFloor) > 0 Then
					
					If FloorTimer(PlayerFloor) = 90 Then PlaySound(GoRightSFX(0))
					If FloorTimer(PlayerFloor) = 600 Then PlaySound(GoRightSFX(1))
					If floorTimer(PlayerFloor) = 1100 Then PlaySound(GoRightSFX(2))
					
					If Distance2(6.4,EntityY(collider),-11.5) < 2.0 And FloorTimer(PlayerFloor) < 2000 Then
						EnemyRedmist = CreateEnemy(6.5,FloorY-0.5,-12.5,RedmistTextures(Rand(0, 2)))	
						EnemyRedmist\speed = 0.015
						CurrEnemy = CreateEnemy(7.5,FloorY, -3.5, MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.015
						PlaySound(HorrorSFX(Rand(0, 9)))
						
						FloorTimer(PlayerFloor) = 4000
					EndIf
					
					If FloorTimer(PlayerFloor) = 2000 Then
						EnemyRedmist = CreateEnemy(6.5,FloorY-0.5,-12.5,RedmistTextures(Rand(0, 2)))	
						EnemyRedmist\speed = 0.015
						CurrEnemy = CreateEnemy(7.5,FloorY, -3.5, MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.015
						PlaySound(HorrorSFX(Rand(0, 9)))
					EndIf
					
					If FloorTimer(PlayerFloor) = 2400 Then
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj
						Delete CurrEnemy
						
						If EnemyRedmist <> Null Then
							FreeEntity EnemyRedmist\collider
							FreeEntity EnemyRedmist\obj
							Delete EnemyRedmist
						EndIf
						FloorTimer(PlayerFloor) = -1
					EndIf
					
					If FloorTimer(PlayerFloor) >= 2000 And FloorTimer(PlayerFloor) < 4000 And EnemyRedmist <> Null Then
						If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider)) < 0.9 Then
							FreeEntity EnemyRedmist\collider
							FreeEntity EnemyRedmist\obj
							Delete EnemyRedmist
						EndIf
					EndIf
					
					If CurrEnemy <> Null Then
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
							KillTimer = max(KillTimer,1)
						EndIf
					EndIf
					If EnemyRedmist <> Null Then
						If Distance2(EntityX(EnemyRedmist\collider),EntityY(EnemyRedmist\collider),EntityZ(EnemyRedmist\collider))<0.8 Then
							KillTimer = max(KillTimer,1)
						EndIf
					EndIf
					
					
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
			Case ACT_CELL
				DO_ANIMATION = False
				If FloorTimer(PlayerFloor)= 1 Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						CurrEnemy = CreateEnemy(startx-4.5,FloorY-0.5,FloorZ+3.2,RedmistTextures(Rand(0, 2)))  
					Else ;pariton
						CurrEnemy = CreateEnemy(startx+4.5,FloorY-0.5,FloorZ-3.2,RedmistTextures(Rand(0, 2)))  
					EndIf
					EntityFX CurrEnemy\obj, 8
					CurrEnemy\speed = 0.0
					FloorTimer(PlayerFloor) = 2
				Else
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
					If CurrEnemy <> Null Then 
						
						;Animate2(CurrEnemy\obj,AnimTime(CurrEnemy\obj),206,250,0.05)
						If Abs(EntityX(camera)-EntityX(CurrEnemy\collider))<0.05 And Rand(300)=1 Then
							If FloorTimer(PlayerFloor) > 3000 Then
								DO_ANIMATION = True
								EntityType CurrEnemy\collider,hit_map
								MoveEntity CurrEnemy\collider, 0, 0, 0.75
								EntityType CurrEnemy\collider,hit_player
								CurrEnemy\speed = 0.02
								KillTimer = max(KillTimer,1)
							Else
								If CurrEnemy\speed = 0 Then PlaySound(HorrorSFX(Rand(0, 9)))
								SetAnimTime(CurrEnemy\obj, 0)
								EntityType CurrEnemy\collider,hit_map
								MoveEntity CurrEnemy\collider, 0, 0, 1.75
								EntityType CurrEnemy\collider,hit_player
								FloorTimer(PlayerFloor)= 3000
							EndIf
						EndIf
					EndIf
					If (FloorTimer(PlayerFloor) Mod 610)= 5 Then 
						If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
							PositionEntity(SoundEmitter,startx-4.5,FloorY-0.5,FloorZ+3.2)
						Else ;pariton
							PositionEntity(SoundEmitter,startx+4.5,FloorY-0.5,FloorZ-3.2)  
						EndIf
						EmitSound(BreathSFX, SoundEmitter)
					EndIf 
				EndIf
			Case ACT_CELL_2
				DO_ANIMATION = False
				If FloorTimer(PlayerFloor)= 1 Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						CurrEnemy = CreateEnemy(5,FloorY-0.5,-10.5,RedmistTextures(Rand(0, 2)))  
					Else ;pariton
						CurrEnemy = CreateEnemy(3,FloorY-0.5,3.5,RedmistTextures(Rand(0, 2)))  
					EndIf
					EntityFX CurrEnemy\obj, 8
					CurrEnemy\speed = 0.0
					FloorTimer(PlayerFloor) = 2
				Else
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
					If CurrEnemy <> Null Then 
						
						;Animate2(CurrEnemy\obj,AnimTime(CurrEnemy\obj),206,250,0.05)
						If Abs(EntityX(camera)-EntityX(CurrEnemy\collider))<0.05 And Rand(300)=1 Then
							If FloorTimer(PlayerFloor) < 3000 Then
								If CurrEnemy\speed = 0 Then PlaySound(HorrorSFX(Rand(0, 9)))
								SetAnimTime(CurrEnemy\obj, 0)
								EntityType CurrEnemy\collider,hit_map
								MoveEntity CurrEnemy\collider, 0, 0, 1.25
								EntityType CurrEnemy\collider,hit_player
								FloorTimer(PlayerFloor)= 3000
							EndIf
						EndIf
					EndIf
					If (FloorTimer(PlayerFloor) Mod 610)= 5 Then 
						If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
							PositionEntity(SoundEmitter,startx-4.5,FloorY-0.5,FloorZ+3.2)
						Else ;pariton
							PositionEntity(SoundEmitter,startx+4.5,FloorY-0.5,FloorZ-3.2)  
						EndIf
						EmitSound(BreathSFX, SoundEmitter)
					EndIf 
				EndIf
			Case ACT_LOCK
				If FloorTimer(PlayerFloor) =1 Then
					door = CreateObject()
					PositionEntity (door, 6.0, FloorY, -0.5)
					EntityTexture door,brickwalltexture	
					EntityType door, hit_map
					ScaleEntity door, 1, 1, 1
					FloorTimer(PlayerFloor) = 2
				EndIf
				If Distance2(FloorX, FloorY, FloorZ)<2.0 And FloorTimer(PlayerFloor) = 2 Then
					wall = CreateObject()
					ScaleEntity(wall, 0.5,1,1.0)
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						PositionEntity (wall, startx-0.5, FloorY, FloorZ)
					Else ;pariton
						PositionEntity (wall, startx+0.5, FloorY, FloorZ)	
					EndIf
					EntityTexture wall,brickwalltexture	
					EntityType wall, hit_map
					GameStarted = True
					BlurTimer = 200
					EmitSound(RoarSFX, SoundEmitter)
					ChannelVolume (MusicChannel,0)
					MusicChannel = PlaySound(Music)
					FloorTimer(PlayerFloor)= 3
				EndIf
				If FloorTimer(PlayerFloor) > 2 And FloorTimer(PlayerFloor) < 250 Then
				
					If FloorTimer(PlayerFloor) < 101 Then ChannelVolume (MusicChannel,(FloorTimer(PlayerFloor)/100.0))
					
					MoveEntity(door, 0,0.007,0)
					
					If FloorTimer(PlayerFloor) = 180 Then EmitSound (AmbientSFX(Rand(0,111)),door)
					If FloorTimer(PlayerFloor) = 340 Then EmitSound (AmbientSFX(Rand(0,111)),door)
					
					r = FloorTimer(PlayerFloor)/10.0
					
					If FloorTimer(PlayerFloor) < 150 Then 
						TurnEntity(camera, Rnd(-1,1), Rnd(-1,1), Rnd(-1,1) )
					Else 
						TurnEntity(camera, Rnd(-0.5,0.5), Rnd(-0.5,0.5), Rnd(-0.5,0.5))
					EndIf
					AmbientLight max(10,Brightness+24-r), max(10,Brightness+24-r), max(10,Brightness+24-r)
					CameraFogRange(camera, 1, 4.2+((min(4.0,250.0/FloorTimer(PlayerFloor)))-1.0))
					
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
				If FloorTimer(PlayerFloor) >= 250 Then
					If FloorTimer(PlayerFloor) = 410 Then EmitSound (AmbientSFX(Rand(0,111)),door)
					If FloorTimer(PlayerFloor) = 510
						PlaySound (AmbientSFX(Rand(0,111)))	
					EndIf		
					If FloorTimer(PlayerFloor) = 610 Then 
						EmitSound (AmbientSFX(Rand(0,111)),SoundEmitter)		
					EndIf		
					If FloorTimer(PlayerFloor) = 720
						PlaySound (AmbientSFX(Rand(0,111)))	
					EndIf
					If FloorTimer(PlayerFloor) = 800 Or FloorTimer(PlayerFloor) = 840 Or FloorTimer(PlayerFloor) = 880
						PlaySound (loudstepsound)	
					EndIf
										
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
					If FloorTimer(PlayerFloor) = 1000 Then FloorTimer(PlayerFloor) = 0
				EndIf
			Case ACT_TRICK1
				If FloorTimer(PlayerFloor)=1 Then 
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						If Distance2(startx-1.5,FloorY-0.5,FloorZ-5.0)<0.25 Then
							CurrEnemy = CreateEnemy(startx-1.5,FloorY-0.5,FloorZ-2.0,RedmistTextures(Rand(0, 2)))  
							CurrEnemy\speed = 0.01
							EntityFX CurrEnemy\obj, 8
							FloorTimer(PlayerFloor)=2
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
					Else ;pariton
						If	Distance2(startx+1.5,FloorY-0.5,FloorZ+5.0)<0.25 Then
							CurrEnemy = CreateEnemy(startx+1.5,FloorY-0.5,FloorZ+2.0,tex1732)  
							CurrEnemy\speed = 0.01
							EntityFX CurrEnemy\obj, 8
							FloorTimer(PlayerFloor)=2
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
					EndIf
				Else
					If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
						KillTimer = max(KillTimer,1)
					EndIf
				EndIf 
			Case ACT_TRICK2
				If FloorTimer(PlayerFloor)=1 Then 	
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						If	Distance2(startx+0.5,FloorY-0.5,FloorZ-5.0)<0.25 Then
							CurrEnemy = CreateEnemy(startx+0.5,FloorY-0.5,FloorZ-2.0,RedmistTextures(Rand(0, 2)))  
							CurrEnemy\speed = 0.01
							EntityFX CurrEnemy\obj, 8
							FloorTimer(PlayerFloor)=2
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
					Else ;pariton
						If Distance2(startx-0.5,FloorY-0.5,FloorZ+5.0)<0.25 Then
							CurrEnemy = CreateEnemy(startx-0.5,FloorY-0.5,FloorZ+2.0,RedmistTextures(Rand(0, 2)))  
							CurrEnemy\speed = 0.01
							EntityFX CurrEnemy\obj, 8
							FloorTimer(PlayerFloor)=2
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
					EndIf		
				Else
					If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
						KillTimer = max(KillTimer,1)
					EndIf	
				EndIf
			Case ACT_TRICK3
				If FloorTimer(PlayerFloor)=1 Then 	
					door = CreateObject()
					PositionEntity (door, 3.001, FloorY, 1.5)
					EntityTexture door,brickwalltexture_a2
					EntityType door, hit_map
					ScaleEntity door, 1, 1, 1
					FloorTimer(PlayerFloor) = 2
				EndIf
				If FloorTimer(PlayerFloor)=2 Then
					If	Distance2(1.5,FloorY-0.5,3.8)<0.5
						EnemyClothWander = CreateEnemy(1.0,FloorY-0.5,2.0,clothwandertex)  
						EnemyClothWander\speed = 0.01
						PlaySound(HorrorSFX(Rand(0, 9)))
						floorTimer(PlayerFloor) = 3
					EndIf	
					If	Distance2(-0.5,FloorY-0.5,3.8)<0.5
						FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
						EnemyClothWander = CreateEnemy(-0.7,FloorY-0.5,2.0,clothwandertex)  
						EnemyClothWander\speed = 0.01
						PlaySound(HorrorSFX(Rand(0, 9)))
						floorTimer(PlayerFloor) = 3
					EndIf
				EndIf
				
				If FloorTimer(PlayerFloor) > 2 Then
					floorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
					
					If Distance2(EntityX(EnemyClothWander\collider),EntityY(EnemyClothWander\collider),EntityZ(EnemyClothWander\collider))<0.8 Then
						KillTimer = max(KillTimer,1)
					EndIf
					
					If FloorTimer(PlayerFloor) = 200 Then
						FreeEntity EnemyClothWander\obj
						FreeEntity EnemyClothWander\collider
						PositionEntity door,0,20,0
						
						Delete EnemyClothWander
						FloorTimer(PlayerFloor) = 0
					EndIf
				EndIf
			Case ACT_TRAP
				If FloorTimer(PlayerFloor)=1 Then 
					CurrObject = CreateObject()
					ScaleEntity(CurrObject, 0.5,1,0.5)
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						PositionEntity (CurrObject, endx+0.5, FloorY, FloorZ)
					Else ;pariton
						PositionEntity (CurrObject, endx-0.5, FloorY, FloorZ)	
					EndIf
					EntityTexture CurrObject,brickwalltexture	
					EntityType CurrObject, hit_map
					FloorTimer(PlayerFloor)= 2
				ElseIf FloorTimer(PlayerFloor)=2
					If Distance2(FloorX, FloorY, FloorZ)<1.0 Then	
						CurrEnemy = CreateEnemy(startX, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.01
						PlaySound(HorrorSFX(Rand(0, 9)))
						FloorTimer(PlayerFloor) = 3
					EndIf
				EndIf
			Case ACT_ROAR
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(EndX, FloorY, FloorZ)<6 Then 
						PositionEntity SoundEmitter,FloorX,FloorY-3,FloorZ
						EmitSound(RoarSFX, SoundEmitter)
						FloorTimer(PlayerFloor) = 51
					EndIf
				Else
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
					If FloorTimer(PlayerFloor)<370 Then
						TranslateEntity(collider, Rnd(-0.005,0.005),Rnd(-0.005,0.005),Rnd(-0.005,0.005))
						TurnEntity(camera, Rnd(-1,1), Rnd(-1,1), Rnd(-1,1))
					Else
						FloorTimer(PlayerFloor) = 0
					EndIf
				EndIf
			Case ACT_WAIT
				DO_ANIMATION = True
				
				If FloorTimer(PlayerFloor) > 2 And FloorTimer(PlayerFloor) < 70
					MoveEntity(CurrObject,0,-0.015,0)
				EndIf
				
				If FloorTimer(PlayerFloor)=1 Then 
					CurrObject = CreateObject()
					ScaleEntity(CurrObject, 0.5,0.5,0.1)
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						PositionEntity (CurrObject, StartX-0.5, FloorY-0.5, FloorZ-1.6)
						CurrEnemy = CreateEnemy(StartX-0.5, FloorY-0.5, FloorZ-2,MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.0
					Else ;pariton
						PositionEntity (CurrObject, StartX+0.5, FloorY-0.5, FloorZ+1.6)	
						CurrEnemy = CreateEnemy(StartX+0.5, FloorY-0.5, FloorZ+2,MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.0
					EndIf
					brick = LoadTexture("GFX\brickwall.jpg")
					ScaleTexture brick,2.5,2
					EntityTexture CurrObject,brick
					EntityType CurrObject, hit_map
					FloorTimer(PlayerFloor)= 2
				ElseIf FloorTimer(PlayerFloor)=70
					CurrEnemy\speed = 0.022
				EndIf
			Case ACT_BEHIND
				DO_ANIMATION = True
				If FloorTimer(PlayerFloor)= 1 And (Distance2(startx-6.0, FloorY - 0.5, FloorZ-2.0) < 2.0 Or Distance2(startx+6.0, FloorY - 0.5, FloorZ+2.0) < 2.0) Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						CurrEnemy = CreateEnemy(StartX-2.5,FloorY-0.5,FloorZ-3.0,RedmistTextures(Rand(0, 2)))  
					Else ;pariton
						CurrEnemy = CreateEnemy(StartX+2.5,FloorY-0.5,FloorZ+3.0,tex1732)  
					EndIf
					PlaySound(BehindSFX)
					EntityFX CurrEnemy\obj, 8
					CurrEnemy\speed = 0.0
					FloorTimer(PlayerFloor) = 50
				EndIf
				If FloorTimer(PlayerFloor) > 1 Then
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
					BlurTimer = min(220,FloorTimer(PlayerFloor))
					If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 0.8 Then
						KillTimer = max(KillTimer,1)
					EndIf
					If EntityInView(CurrEnemy\collider, camera) And EntityVisible(CurrEnemy\collider,camera) Then
						CurrEnemy\speed = 0.3	
					EndIf
				EndIf
				If FloorTimer(PlayerFloor)=600 Then
					FloorTimer(PlayerFloor)=0
					
					FreeEntity CurrEnemy\collider
					FreeEntity CurrEnemy\obj						
					Delete CurrEnemy
				EndIf
				
			Case ACT_CHARGE173
				DO_ANIMATION = True
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(FloorX, FloorY, FloorZ)<2.0 Then
						CurrEnemy = CreateEnemy(EndX, FloorY-0.5, FloorZ, RedmistTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.1
						EntityFX CurrEnemy\obj,8
						PlaySound(HorrorSFX(Rand(0, 9)))
						FloorTimer(PlayerFloor) = 2
					EndIf
				EndIf
				If FloorTimer(PlayerFloor) = 2 Then
					If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj
						Delete CurrEnemy
							
						FloorTimer(PlayerFloor)=0
					EndIf
				EndIf
			Case ACT_CORNER_TRAP
				If FloorTimer(PlayerFloor) >= 1 Then
						
					If FloorTimer(PlayerFloor) = 2 Then
						DO_ANIMATION = True
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
							FreeEntity CurrEnemy\collider
							FreeEntity CurrEnemy\obj
							Delete CurrEnemy								
							FloorTimer(PlayerFloor)=0
							PlaySound(HorrorSFX(Rand(0, 9)))
						EndIf
					Else
						If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then
							If EntityX(collider) < 5.7 And EntityX(collider) > 5.0 And EntityZ(collider) > -10.2 And EntityZ(collider) < -9.6 Then
								CurrEnemy = CreateEnemy(2, FloorY-0.5, -11.5, RedmistTextures(Rand(0, 2)))
								CurrEnemy\speed = 0.05
								EntityFX CurrEnemy\obj,8
								FloorTimer(PlayerFloor) = 2
							EndIf
						Else
							If EntityX(collider) < 2.8 And EntityX(collider) > 2.2 And EntityZ(collider) > 2.6 And EntityZ(collider) < 2.9 Then
								CurrEnemy = CreateEnemy(5.5, FloorY-0.5, 4.4, RedmistTextures(Rand(0, 2)))
								CurrEnemy\speed = 0.05
								EntityFX CurrEnemy\obj,8
								FloorTimer(PlayerFloor) = 2
							EndIf
						EndIf
					EndIf
				EndIf
			Case ACT_DARKNESS
				If FloorTimer(PlayerFloor)= 1 Then
					If Distance2(FloorX, FloorY, FloorZ)<1.0 Then
						wall = CreateObject()
						ScaleEntity(wall, 0.5,1,0.5)
						If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
							PositionEntity (wall, startx-0.5, FloorY, FloorZ)
						Else ;pariton
							PositionEntity (wall, startx+0.5, FloorY, FloorZ)	
						EndIf
						EntityTexture wall,brickwalltexture	
						EntityType wall, hit_map
						
						wall = CreateObject()
						ScaleEntity(wall, 0.5,1,0.5)
						If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
							PositionEntity (wall, ENDX+0.5, FloorY, FloorZ)
						Else ;pariton
							PositionEntity (wall, endx-0.5, FloorY, FloorZ)	
						EndIf
						EntityTexture wall,brickwalltexture	
						EntityType wall, hit_map					
						
						EmitSound (StoneSFX,wall)
						FloorTimer(PlayerFloor)= 2
					EndIf		
				ElseIf FloorTimer(PlayerFloor)<600
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor)+1
					temp#=max(Brightness-(FloorTimer(PlayerFloor)/600.0)*Brightness,10)
					AmbientLight temp,temp,temp
					
					If FloorTimer(PlayerFloor) = 600 Then
						CurrEnemy = CreateEnemy(FloorX, FloorY-0.5, FloorZ,MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.01
						PlaySound(HorrorSFX(Rand(0, 9)))
						FloorTimer(PlayerFloor) = 601
					EndIf
				Else
					If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.7 Then
						KillTimer = max(KillTimer,1)
					EndIf
				EndIf
				
			Case ACT_BLUR
				If FloorTimer(PlayerFloor)= 1 Then
					DO_ANIMATION = True
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen 
						CurrEnemy = CreateEnemy(1.55,FloorY+0.8,-15.45,EyekillerTextures(Rand(0, 2)))
					Else ;pariton
						CurrEnemy = CreateEnemy(1.55,FloorY+0.8,-15.45,EyekillerTextures(Rand(0, 2)))
					EndIf
					EntityFX CurrEnemy\obj, 8
					CurrEnemy\speed = 0.0
					FloorTimer(PlayerFloor) = 2
				EndIf
				If FloorTimer(PlayerFloor) = 2 And Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 5.0 Then
					BlurTimer = 270
				EndIf
				If Not (ChannelPlaying(SoundChannel)) Then
					SoundChannel = EmitSound(eyekillerSFX,CurrEnemy\collider)
				EndIf
			Case ACT_BROKENROOM
				If FloorTimer(PlayerFloor)=1 Then
					Radio = LoadMesh("GFX\map\radio.x")
					SoundChannel = EmitSound(radioMusic,Radio)
					ScaleEntity(Radio, 1,1,1)
					PositionEntity Radio,-1.15,FloorY-0.5,-18.8
					EntityType Radio, hit_map
					RotateEntity(Radio,0,60,0)
					FloorTimer(PlayerFloor)=2
				EndIf
				If FloorTimer(PlayerFloor)>1 Then
					DO_ANIMATION = False
					ChannelVolume SoundChannel, max(0.0,1.0-(Distance2(EntityX(Radio),EntityY(Radio),EntityZ(Radio))/8.0))
					
					If Distance2(EntityX(Radio),EntityY(Radio),EntityZ(Radio)) < 1.5 And FloorTimer(PlayerFloor) = 2 Then
						CurrEnemy = CreateEnemy(3.0,FloorY+0.8,-18.5,RedmistTextures(Rand(0, 2)))
						EntityFX CurrEnemy\obj, 8
						CurrEnemy\speed = 0.0
						FloorTimer(PlayerFloor) = 3
					EndIf
				EndIf
			Case ACT_MAZE
				DO_ANIMATION = True
				If FloorTimer(PlayerFloor) = 1 Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then
						CurrEnemy = CreateEnemy(10.5,FloorY-0.5,-17.5,MentalTextures(Rand(0, 2)))
					Else
						CurrEnemy = CreateEnemy(-2.5,FloorY-0.5,11.5,MentalTextures(Rand(0, 2)))
					EndIf
					CurrEnemy\speed = 0.02
					FloorTimer(PlayerFloor)=2
				EndIf
				If FloorTimer(PlayerFloor) = 2 And EntityInView(CurrEnemy\collider, camera) And Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider)) < 3.0 Then
					PlaySound(HorrorSFX(Rand(0, 9)))
					FloorTimer(PlayerFloor)=3
				EndIf
				
				If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then
					If Distance2(10.5,FloorY-0.5,-17.5) < 1.0 Then
						FloorTimer(PlayerFloor) = 0
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj
						Delete CurrEnemy
					EndIf
				Else
					If Distance2(-2.5,FloorY-0.5,11.5) < 1.0 Then
						FloorTimer(PlayerFloor) = 0
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj
						Delete CurrEnemy
					EndIf
				EndIf
				
				If CurrEnemy<>Null Then
					If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
						KillTimer = max(KillTimer,1)
					EndIf
				EndIf
			Case ACT_ELEVATOR
				If FloorTimer(PlayerFloor) = 1 Then
					DO_ANIMATION = True
					wall = CreateObject()
					ScaleEntity(wall, 0.5,1,0.5)
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						PositionEntity (wall, 3.5, FloorY, -7.5)
					Else ;pariton
						PositionEntity (wall, 4.5, FloorY, 0.5)	
					EndIf
					EntityTexture wall,brickwalltexture	
					EntityType wall, hit_map
					
					FloorTimer(PlayerFloor) = 2
				EndIf
				If FloorTimer(PlayerFloor) = 2 Then
					If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
						If Distance2(4.5,FloorY,-9) < 1.0 And (Not EntityInView(wall, camera)) Then
							PositionEntity(wall, 4.3, FloorY, -7.5)
							PlaySound(StoneSFX)
							FloorTimer(PlayerFloor) = 3
						EndIf
					Else ;pariton
						If Distance2(startx+3.5,FloorY,2) < 1.0 And (Not EntityInView(wall, camera)) Then
							PositionEntity(wall, 3.7, FloorY, 0.5)
							PlaySound(StoneSFX)
							FloorTimer(PlayerFloor) = 3
						EndIf
					EndIf
				EndIf
				If FloorTimer(PlayerFloor) >= 3 And FloorTimer(PlayerFloor) <= 80 Then
					AmbientLight Max(BRIGHTNESS-FloorTimer(PlayerFloor),30),Max(BRIGHTNESS-FloorTimer(PlayerFloor),30),Max(BRIGHTNESS-FloorTimer(PlayerFloor),30)
					Floortimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
				If FloorTimer(PlayerFloor) > 80 Then
					FloorTimer(PlayerFloor) = FloorTimer(PlayerFloor) + 1
					
					If FloorTimer(PlayerFloor) = 100 Then PlaySound(WaitSFX)
					If FloorTimer(PlayerFloor) = 200 Then
						CurrEnemy = CreateEnemy(startx,FloorY-0.5,FloorZ,MentalTextures(Rand(0, 2)))
						CurrEnemy\speed = 0.05
					EndIf
					If FloorTimer(PlayerFloor) = 300 Then DO_ANIMATION = False : CurrEnemy\speed = 0.00
					If FloorTimer(PlayerFloor) = 400 Then 
						DO_ANIMATION = True
						AmbientLight 255,255,255
						If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then
							PositionEntity(CurrEnemy\collider, 4.5,FloorY-0.5,-7.5)
							PositionEntity(CurrEnemy\obj, 4.5,FloorY-0.5,-7.5)
						Else
							PositionEntity(CurrEnemy\collider, startx+3.5,FloorY-0.5,1.0)
							PositionEntity(CurrEnemy\obj, startx+3.5,FloorY-0.5,1.0)
						EndIf
						PositionEntity wall,0,10,0
						CurrEnemy\speed = 0.01
						PlaySound(HorrorSFX(Rand(0, 9)))
					EndIf
					If FloorTimer(PlayerFloor) = 470 Then
						FloorTimer(PlayerFloor) = 0
						AmbientLight BRIGHTNESS,BRIGHTNESS,BRIGHTNESS
						FreeEntity CurrEnemy\collider
						FreeEntity CurrEnemy\obj
						Delete CurrEnemy
					EndIf
					
					If CurrEnemy <> Null Then
						If Distance2(EntityX(CurrEnemy\collider),EntityY(CurrEnemy\collider),EntityZ(CurrEnemy\collider))<0.8 Then
							KillTimer = max(KillTimer,1)
						EndIf
					EndIf
				EndIf
				
			Case ACT_HALLWAYTRAP
				DO_ANIMATION = True
				If Floortimer(PlayerFloor) = 1 And Distance2(4.5, FloorY, -7.5)<1.0 Then
					PlaySound(HorrorSFX(Rand(0, 9)))
					EnemyClothWander = CreateEnemy(7.5,FloorY-0.5,-7.5,clothwandertex)
					EnemyClothWander\speed = 0.012
					EnemyClothWander2 = CreateEnemy(0.5,FloorY-0.5,-7.5,clothwandertex)
					EnemyClothWander2\speed = 0.012

					FloorTimer(PlayerFloor) = 2
				EndIf
				If floortimer(PlayerFloor) > 1 Then
					If distance2(EntityX(EnemyClothWander\collider), EntityY(EnemyClothWander\collider), EntityZ(EnemyClothWander\collider)) < 0.8 Then
						KillTimer = max(1,KillTimer)
						FloorTimer(PlayerFloor) = 2
					EndIf
					If distance2(EntityX(EnemyClothWander2\collider), EntityY(EnemyClothWander2\collider), EntityZ(EnemyClothWander2\collider)) < 0.8 Then
						KillTimer = max(1,KillTimer)
						FloorTimer(PlayerFloor) = 2
					EndIf
					floortimer(playerFloor) = FloorTimer(PlayerFloor) + 1
				EndIf
				If FloorTimer(PlayerFloor) = 200 Then
					FreeEntity EnemyClothWander\obj
					FreeEntity EnemyClothWander\collider
					FreeEntity EnemyClothWander2\obj
					FreeEntity EnemyClothWander2\collider
					
					Delete EnemyClothWander
					Delete EnemyClothWander2
					
					floortimer(playerfloor) = 0
				EndIf
			
		End Select
		
	EndIf
End Function

Function Distance2#(x1#, y1#, z1#) 
	Local vxcomp# = Abs(x1 - EntityX(collider))
	Local vycomp# = Abs(y1 - EntityY(collider))
	Local vzcomp# = Abs(z1 - EntityZ(collider))
	Return Sqr(vxcomp * vxcomp + vycomp * vycomp + vzcomp * vzcomp)
End Function

Function RestartGame()
	For room.FLOORS = Each FLOORS
		FreeEntity room\mesh
		FreeEntity room\sign
		Delete room
	Next
	For enemy.ENEMIES = Each ENEMIES 
		FreeEntity enemy\obj
		FreeEntity enemy\collider
		Delete enemy
	Next
	For glimpse.GLIMPSES = Each GLIMPSES 
		FreeEntity glimpse\obj
		Delete glimpse
	Next
	
	For obj.OBJECTS = Each OBJECTS 
		FreeEntity obj\mesh
		Delete obj
	Next

	For i = 0 To flooramount-1
		FloorTimer(i) = 0
		FloorActions(i) = 0
	Next
	CameraFogRange(camera, 1, 6.3)
	GameStarted = False

	EntityType collider, 0
	PositionEntity collider,-2.5,-1.3,-0.5
	EntityRadius (collider, ColliderXRadius,ColliderYRadius)
	EntityType collider, hit_player
	
	IsPaused = False
	PauseTimer = 0
	PauseBool = False
	GAME_END = False
	dropspeed# = 0.000
	steptimer = 0
	breathtimer = 0
	crouchtimer = 0
	IsCrouched = False
	killtimer = 0
	MusicTimer = 100
	blurtimer = 350
	introtimer = 150
	DeepMusicON = False
	
	AmbientLight Brightness+34,Brightness+34,Brightness+34
	
	ChannelVolume MusicChannel,0.00
	CreateMap(flooramount)
	CreateGlimpses()
	
	If area = "The Second" Then
		PositionEntity collider,0.5,-280.5,-0.5
		MUSIC_ON = True
		MusicTimer = 0
		GameStarted = True
		AmbientLight BRIGHTNESS,BRIGHTNESS,BRIGHTNESS
		DeepMusicON = True
	Else
		If sector = "Lower" Then
			PositionEntity collider,7.7,-98.5,-13.0
			GameStarted = True
			DeepMusicON = True
		EndIf
	EndIf
	
	PlayerFloor = (-EntityY(collider)-0.3)/2+0.1
	
	MoveMouse viewport_center_x, viewport_center_y
	MoveEntity collider, 0.0, 0.1, 0.0
	RotateEntity collider, 0.0, -90.0, 0.0
	
	StopChannel MusicChannel
	StopChannel SoundChannel
	StopChannel ChaseChannel
	
End Function

Function Kill()
	If KillTimer = 1 Then PlaySound DeathSFX
	
	KillTimer=KillTimer+1
	
	AmbientLight 255-KillTimer, 100-KillTimer, 100-KillTimer
	If KillTimer < 90 Then 
		PositionEntity camera, EntityX(collider), EntityY(collider)+0.85-(KillTimer/100.0), EntityZ(collider) 
		MoveEntity camera, side, up, 0
		RotateEntity camera, -KillTimer, EntityYaw(camera), EntityRoll(collider)-(KillTimer/2)
		ChannelVolume MusicChannel,1.00-((KillTimer+1.0)/90.0)
		ChannelVolume SoundChannel,1.00-((KillTimer+1.0)/90.0)
		ChannelVolume SoundChannel2,1.00-((KillTimer+1.0)/90.0)
	EndIf
	
	If KillTimer>90 Then 
		AmbientLight 150-KillTimer, 0, 0
		If KillTimer > 150 Then
		
			AmbientLight 0, 0, 0
			
			Local FloorZ#,FloorY#,StartX#,EndX#
			
			If PlayState=GAME_SERVER Then
				PlayerFloor=max(PlayerFloor/2,1)
				FloorY#=-(PlayerFloor-1)*2-1.0
				If Floor(PlayerFloor/2.0)=Ceil(PlayerFloor/2.0) Then ;parillinen
					FloorZ#=-6.54
					StartX# = 7.2 
					EndX# = 0.8
				Else ;pariton
					FloorZ#=-0.31
					StartX# = 0.8
					EndX# = 7.2
				EndIf
				
				PositionEntity collider,Rnd(StartX,EndX),FloorY+1.7,FloorZ,True
				;ResetEntity collider
				If LinePick(EntityX(collider,True),EntityY(collider,True),EntityZ(collider,True),0.0,-40.0,0.0,0.0)<>0 Then
					PositionEntity collider,EntityX(collider,True),PickedY()+0.5,EntityZ(collider,True),True
					;ResetEntity collider
					DebugLog "PICKED!"
				EndIf
				
				KillTimer = 0
				AmbientLight Brightness,Brightness,Brightness
				UpdateWorld
				
				Return
			EndIf
			
			If KillTimer > 240 And PlayState = GAME_SOLO Then
				temp = False
				PauseTimer = 0
				While temp = False
					Color 15,15,15
					Cls
					RenderWorld
					UpdateBlur(1.00)
					
					SetFont signfont
					Color 65,15,15
					Text (GraphicsWidth () / 2)-227,(GraphicsHeight () / 2)-115,"YOU DIED"
					Color PauseTimer,PauseTimer,PauseTimer
					Text (GraphicsWidth () / 2)-224,(GraphicsHeight () / 2)-118,"YOU DIED"
					
					SetFont font1
					Color 15,15,15
					Text (GraphicsWidth () / 2)-114,(GraphicsHeight () / 2)+2,"Press ESC to restart"
					Color PauseTimer,PauseTimer,PauseTimer
					Text (GraphicsWidth () / 2)-111,(GraphicsHeight () / 2),"Press ESC to restart"
					
					Color 15,15,15
					Text (GraphicsWidth () / 2)-114,(GraphicsHeight () / 2)+21,"Press SPACEBAR to quit"
					Color PauseTimer,PauseTimer,PauseTimer
					Text (GraphicsWidth () / 2)-111,(GraphicsHeight () / 2)+19,"Press SPACEBAR to quit"
					
					Flip
					Delay 20
					
					If PauseTimer > 200 Then
						PauseBool = True
					ElseIf PauseTimer < 100 Then
						PauseBool = False
					EndIf
					
					If PauseBool Then
						PauseTimer = PauseTimer - 1
					Else
						PauseTimer = PauseTimer + 1
					EndIf
					
					If KeyHit(1) Then
						IsPaused = False
						temp = True
						PauseTimer = 0
						MoveMouse viewport_center_x, viewport_center_y
					EndIf
					If KeyDown(57) Then
						End
					EndIf
				Wend
				RestartGame()
			EndIf
		EndIf
	EndIf
End Function 

Function TeleportToFloor(num)
	Local FloorZ#,FloorY#,StartX#,EndX#
			
	PlayerFloor=max(num/2,1)
	FloorY#=-(num-1)*2-1.0
	If Floor(num/2.0)=Ceil(num/2.0) Then ;parillinen
		FloorZ#=-6.54
		StartX# = 7.2 
		EndX# = 0.8
	Else ;pariton
		FloorZ#=-0.31
		StartX# = 0.8
		EndX# = 7.2
	EndIf
	
	If num = 200 Then
		startx = 1
		floorz = 1.2
		floory = floory - 0.28
	EndIf
	
	EntityType collider, hit_map
	PositionEntity collider,startX,floory,floorz
	EntityType collider, hit_player
	dropspeed = 0
End Function

Function GetINIString$(file$, section$, parameter$)
	Local TemporaryString$ = ""
	Local f = ReadFile(file)
	
	While Not Eof(f)
		If Lower(ReadLine(f)) = "[" + Lower(section) + "]" Then
			Repeat
				TemporaryString = ReadLine(f)
			 	If Lower(Trim(Left(TemporaryString, max(Instr(TemporaryString, "=") - 1, 0)))) = Lower(parameter) Then
					CloseFile f
					Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
				EndIf
			Until Left(TemporaryString, 1) = "[" Or Eof(f)
			CloseFile f
			Return ""
		EndIf
	Wend
	
	CloseFile f
End Function

Function GetINIInt$(file$, section$, parameter$)
	Local Stri$= Trim(GetINIString(file$, section$, parameter$))
	If Lower(Stri) = "true" Then
		Return 1
	ElseIf Lower(Stri) = "false"
		Return 0
	Else
		Return Int(Stri)
	EndIf
End Function

Function GetINIFloat$(file$, section$, parameter$)
	Return Float(GetINIString(file$, section$, parameter$))
End Function

Function WordWrap%(A$,X,Y,W,H,Leading=0, center = 0)
	;Display A$ starting at X,Y - no wider than W and no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	;To Do (if needed): force break if single word is too big to fit on line (currently function will hang if this happens)
	LinesShown=0
	StrHeight=StringHeight(A$)+Leading
	FittedText$ = ""
	While Len(A)>0
		space=Instr(A$," ")
		If space=0 Then space=Len(A$)
		temp$=Left$(A$,space)
		trimmed$=Trim$(temp);we might ignore a final space
		extra=0;we haven't ignored it yet
		;ignore final space if doing so would make a word fit at end of line:
		If (StringWidth (b$+temp$)>W) And (StringWidth (b$+trimmed$)<=W) Then temp=trimmed:extra=1
		If StringWidth (b$+temp$)>W Then;too big, so print what will fit
			If center Then Text X + (W Shr 1),LinesShown * StrHeight + Y,b$,True Else Text X,LinesShown * StrHeight + Y,b$
			FittedText = FittedText + b
			LinesShown=LinesShown+1
			b$=""
		Else;append it to B$ (which will eventually be printed) and remove it from A$
			b$=b$+temp$
			A$=Right$(A$,Len(A$)-(Len(temp$)+extra))
		EndIf
		If ((LinesShown+1)*StrHeight)>H Then Exit;the next line would be too tall, so leave
	Wend
	If (b$<>"")And((LinesShown+1)<=H) Then 
		If center Then Text X + (W Shr 1),LinesShown * StrHeight + Y,b$,True Else Text X,LinesShown * StrHeight + Y,b$
		;Text X,LinesShown*StrHeight+Y,b$;print any remaining text if it'll fit vertically
		FittedText = FittedText + b
	EndIf
	Return Len(FittedText)
End Function

Function PutINIValue%(file$, INI_sSection$, INI_sKey$, INI_sValue$)
	; Returns: True (Success) Or False (Failed)
	
	INI_sSection = "[" + Trim$(INI_sSection) + "]"
	Local INI_sUpperSection$ = Upper$(INI_sSection)
	INI_sKey = Trim$(INI_sKey)
	INI_sValue = Trim$(INI_sValue)
	Local INI_sFilename$ = file$
	
	; Retrieve the INI Data (If it exists)
	
	Local INI_sContents$ = INI_FileToString(INI_sFilename)
	
		; (Re)Create the INI file updating/adding the SECTION, KEY And VALUE
	
	Local INI_bWrittenKey% = False
	Local INI_bSectionFound% = False
	Local INI_sCurrentSection$ = ""
	
	Local INI_lFileHandle% = WriteFile(INI_sFilename)
	If INI_lFileHandle = 0 Then Return False ; Create file failed!
	
	Local INI_lOldPos% = 1
	Local INI_lPos% = Instr(INI_sContents, Chr$(0))
	
	While (INI_lPos <> 0)
		
		Local INI_sTemp$ = Mid$(INI_sContents, INI_lOldPos, (INI_lPos - INI_lOldPos))
		
		If (INI_sTemp <> "") Then
			
			If Left$(INI_sTemp, 1) = "[" And Right$(INI_sTemp, 1) = "]" Then
				
					; Process SECTION
				
				If (INI_sCurrentSection = INI_sUpperSection) And (INI_bWrittenKey = False) Then
					INI_bWrittenKey = INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue)
				End If
				INI_sCurrentSection = Upper$(INI_CreateSection(INI_lFileHandle, INI_sTemp))
				If (INI_sCurrentSection = INI_sUpperSection) Then INI_bSectionFound = True
				
			Else
				DebugLog INI_sTemp
				If Left(INI_sTemp, 1) = ":" Then
					WriteLine INI_lFileHandle, INI_sTemp
				Else
						; KEY=VALUE				
					Local lEqualsPos% = Instr(INI_sTemp, "=")
					If (lEqualsPos <> 0) Then
						If (INI_sCurrentSection = INI_sUpperSection) And (Upper$(Trim$(Left$(INI_sTemp, (lEqualsPos - 1)))) = Upper$(INI_sKey)) Then
							If (INI_sValue <> "") Then INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
							INI_bWrittenKey = True
						Else
							WriteLine INI_lFileHandle, INI_sTemp
						End If
					End If
				EndIf
				
			End If
			
		End If
		
			; Move through the INI file...
		
		INI_lOldPos = INI_lPos + 1
		INI_lPos% = Instr(INI_sContents, Chr$(0), INI_lOldPos)
		
	Wend
	
		; KEY wasn;t found in the INI file - Append a New SECTION If required And create our KEY=VALUE Line
	
	If (INI_bWrittenKey = False) Then
		If (INI_bSectionFound = False) Then INI_CreateSection INI_lFileHandle, INI_sSection
		INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
	End If
	
	CloseFile INI_lFileHandle
	
	Return True ; Success
End Function

Function INI_FileToString$(INI_sFilename$)
	Local INI_sString$ = ""
	Local INI_lFileHandle%= ReadFile(INI_sFilename)
	If INI_lFileHandle <> 0 Then
		While Not(Eof(INI_lFileHandle))
			INI_sString = INI_sString + ReadLine$(INI_lFileHandle) + Chr$(0)
		Wend
		CloseFile INI_lFileHandle
	End If
	Return INI_sString
End Function

Function INI_CreateSection$(INI_lFileHandle%, INI_sNewSection$)
	If FilePos(INI_lFileHandle) <> 0 Then WriteLine INI_lFileHandle, "" ; Blank Line between sections
	WriteLine INI_lFileHandle, INI_sNewSection
	Return INI_sNewSection
End Function

Function INI_CreateKey%(INI_lFileHandle%, INI_sKey$, INI_sValue$)
	WriteLine INI_lFileHandle, INI_sKey + " = " + INI_sValue
	Return True
End Function

Function MouseOn%(x%, y%, width%, height%)
	If MouseX() > x And MouseX() < x + width Then
		If MouseY() > y And MouseY() < y + height Then
			Return True
		End If
	End If
	Return False
End Function

Function rInput$(aString$)
	Local value% = GetKey()
	Local length% = Len(aString$)
	
	If value = 8 Then
		value = 0
		If length > 0 Then aString$ = Left(aString, length - 1)
	EndIf
	
	If value = 13 Or value = 0 Then
		Return aString$
	ElseIf value > 0 And value < 7 Or value > 26 And value < 32 Or value = 9
		Return aString$
	Else
		aString$ = aString$ + Chr(value)
		Return aString$
	End If
End Function

Function InputBox$(x%, y%, width%, height%, Txt$, ID% = 1, canclickoff%=True)
	Color (255, 255, 255)
	Rect(x,y,width,height,True)
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
		If MouseHit1 Then SelectedInputBox = ID : FlushKeys
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)
	
	If (Not MouseOnBox) And MouseHit1 And canclickoff And SelectedInputBox = ID Then SelectedInputBox = 0
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt)
		If (MilliSecs() Mod 800) < 400 Then Rect (x + StringWidth(Txt) + 2, y + height / 2 - 5, 2, 12)
	EndIf	
	
	Color 255,255,255
	Text(x + 2, y + height / 2, Txt, False, True)
	
	Return Txt
End Function

Function Download(link$, savepath$ = "", savefile$ = "")
	;Strip protocol and return false if not "http"
	inst = Instr(link$, "://")
	If inst Then
		If Lower(Trim(Left(link$, inst - 1))) <> "http" Then Return False
		link$ = Right(link$, Len(link$) - inst - 2)
	EndIf
	
	;Seperate host from link
	inst = Instr(link$, "/")
	If inst = 0 Then Return False
	host$ = Trim(Left(link$, inst - 1))
	link$ = Trim(Right(link$, Len(link$) - inst + 1))
	
	;Seperate path and file from the link
	For i = Len(link$) To 1 Step -1
		If Mid(link$, i, 1) = "/" Then
			link_path$ = Trim(Left(link$, i))
			link_file$ = Right(link$, Len(link$) - i)
			Exit
		EndIf
	Next
	If link_file$ = "" Then Return False
	If savefile$ = "" Then savefile$ = link_file$
	
	;Open TCP stream
	TCPTimeouts 2000,0
	tcp = OpenTCPStream(host$, 80)
	If tcp = 0 Then Return False
	WriteLine tcp, "GET " + link_path$ + link_file$ + " HTTP/1.1" + Chr(13) + Chr(10) + "Host: " + host$ + Chr(13) + Chr(10) + "User-Agent: Download_Function_By_bytecode77" + Chr(13) + Chr(10)
	
	If savepath <> "" Then
		If Right(savepath,1)<>"/" And Right(savepath,1)<>"\" Then
			savepath=savepath+ "/"
		EndIf
		If FileType(Left(savepath,Len(savepath)-1))=0 Then
			CreateDir(savepath)
		EndIf
	EndIf
	
	;Download file
	l$ = ReadLine(tcp)
	inst1 = Instr(l$, " ")
	inst2 = Instr(l$, " ", inst1 + 1)
	num = Mid(l$, inst1, inst2 - inst1)
	Select num
		Case 200
			conlen = -1
			chunk = False
			
			Repeat
				l$ = Trim(ReadLine(tcp))
				If l$ = "" Then Exit
				
				inst = Instr(l$, ":")
				l1$ = Trim(Left(l$, inst - 1))
				l2$ = Trim(Right(l$, Len(l$) - inst))
				Select Lower(l1$)
					Case "content-length"
						conlen = l2$
					Case "transfer-encoding"
						If Lower(l2$) = "chunked" Then chunk = True
				End Select
			Forever
			
			If conlen = 0 Then
				file = WriteFile(savepath$ + savefile$)
				CloseFile file
				CloseTCPStream tcp
				Return True
			ElseIf conlen > 0 Then
				timer = 0
				file = WriteFile(savepath$ + savefile$)
				bnk = CreateBank(4096)
				pos = 0
				Repeat
					avail = conlen - pos
					If avail > 4096 Then
						ReadBytes bnk, tcp, 0, 4096
						WriteBytes bnk, file, 0, 4096
						pos = pos + 4096
						
						If timer Mod 10 = 0 Then
							Cls
							Text 320,250,"Downloading [" +Int(Float(conlen-avail)/Float(conlen)*100)+ "%]",True,True
							Flip
						EndIf
						timer = timer + 1
					Else
						ReadBytes bnk, tcp, 0, avail
						WriteBytes bnk, file, 0, avail
						Exit
					EndIf
					
				Forever
				FreeBank bnk
				CloseFile file
				CloseTCPStream tcp
				Return True
			ElseIf chunk Then
				file = WriteFile(savepath$ + savefile$)
				bnk = CreateBank(4096)
				
				Repeat
					l$ = Trim(Upper(ReadLine(tcp)))
					ln = 0
					For i = 1 To Len(l$)
						ln = 16 * ln + Instr("123456789ABCDEF", Mid$(l$, i, 1))
					Next
					If ln = 0 Then Exit
					
					If BankSize(bnk) < ln Then ResizeBank bnk, ln
					ReadBytes bnk, tcp, 0, ln
					WriteBytes bnk, file, 0, ln
					ReadShort(tcp)
				Forever
				
				FreeBank bnk
				CloseFile file
				CloseTCPStream tcp
				Return True
			Else
				CloseTCPStream tcp
				Return False
			EndIf
		Case 301, 302
			Repeat
				l$ = Trim(ReadLine(tcp))
				If l$ = "" Then Exit
				
				inst = Instr(l$, ":")
				l1$ = Trim(Left(l$, inst - 1))
				l2$ = Trim(Right(l$, Len(l$) - inst))
				Select Lower(l1$)
					Case "location"
						CloseTCPStream tcp
						Return Download(l2$, savepath$, savefile$)
				End Select
			Forever
		Default
			CloseTCPStream tcp
			Return False
	End Select
End Function