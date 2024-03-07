Global Litex,BumpTex,AmbientTex,MeshTex,Reftex,lbox,Intencity,Bump_ref

Function InitBump ( LightCube$, SpecularTex$, light, cam, Blic_size#=20, Intencity#=1,AmbColorR, AmbColorG, AmbColorB )
	Local argb
	
	lbox = LoadLightBox(LightCube$,"png");

	Bump_ref = LoadTexture( SpecularTex$,1)
	TextureBlend Bump_ref,2
	
  
	AmbientTex=CreateTexture(1,1,1)
	TextureBlend   AmbientTex,3
	
	argb= (((AmbColorR Shl 8) Or AmbColorG) Shl 8) Or AmbColorB
	WritePixel 0,0,argb ,TextureBuffer(  AmbientTex)
	
 
	Litex =   CreateTexture( 32,32, 128+9)
	SetCubeMode   Litex,2
	TextureBlend  Litex,1
	
   
	Reftex =  CreateTexture( 64,64, 128+9)
	SetCubeMode   Reftex,1
	TextureBlend  Reftex,1
	
	
	RenderLight (   Litex,     light, cam, lbox, Intencity )
	RenderLight ( Reftex,   light, cam, lbox, .0, 1, Blic_size, Intencity,   Bump_ref )
	
End Function

Function LoadLightBox( file$, ext$ )
	m=CreateMesh()
	;front face
	b=LoadBrush( file$+"_pz."+ext$,49 )
	s=CreateSurface( m,b )
	AddVertex s,-1,+1,-1,1,1:AddVertex s,+1,+1,-1,0,1
	AddVertex s,+1,-1,-1,0,0:AddVertex s,-1,-1,-1,1,0
	AddTriangle s,0,1,2:AddTriangle s,0,2,3:
	FreeBrush b
	;right face
	b=LoadBrush( file$+"_nx."+ext$,49 )
	s=CreateSurface( m,b )
	AddVertex s,+1,+1,-1,1,1:AddVertex s,+1,+1,+1,0,1
	AddVertex s,+1,-1,+1,0,0:AddVertex s,+1,-1,-1,1,0
	AddTriangle s,0,1,2:AddTriangle s,0,2,3
	FreeBrush b
	;back face
	b=LoadBrush( file$+"_nz."+ext$,49 )
	s=CreateSurface( m,b )
	AddVertex s,+1,+1,+1,1,1:AddVertex s,-1,+1,+1,0,1
	AddVertex s,-1,-1,+1,0,0:AddVertex s,+1,-1,+1,1,0
	AddTriangle s,0,1,2:AddTriangle s,0,2,3
	FreeBrush b
	;left face
	b=LoadBrush( file$+"_px."+ext$,49 )
	s=CreateSurface( m,b )
	AddVertex s,-1,+1,+1,1,1:AddVertex s,-1,+1,-1,0,1
	AddVertex s,-1,-1,-1,0,0:AddVertex s,-1,-1,+1,1,0
	AddTriangle s,0,1,2:AddTriangle s,0,2,3
	FreeBrush b
	;top face
	b=LoadBrush( file$+"_ny."+ext$,49 )
	s=CreateSurface( m,b )
	AddVertex s,-1,+1,+1,1,1:AddVertex s,+1,+1,+1,0,1
	AddVertex s,+1,+1,-1,0,0:AddVertex s,-1,+1,-1,1,0
	AddTriangle s,0,1,2:AddTriangle s,0,2,3
	FreeBrush b
	;bottom face	
	b=LoadBrush( file$+"_py."+ext$,49 )
	s=CreateSurface( m,b )
	AddVertex s,-1,-1,-1,1,1:AddVertex s,+1,-1,-1,0,1
	AddVertex s,+1,-1,+1,0,0:AddVertex s,-1,-1,+1,1,0
	AddTriangle s,0,1,2:AddTriangle s,0,2,3
	FreeBrush b
	FlipMesh m
	EntityFX m,1
	Return m
End Function

Function RenderLight ( Reftex, light, cam, mesh, Intencity#=1, Mode=0,size#=10, IntencitySpr#=1, Ref=0)
	Local BlicSpr,fxcam
	
	fxcam = CreateCamera()
	
	TFormNormal 0,0,1,light,0
	
	CameraViewport fxcam, 0,0,TextureWidth(Reftex), TextureHeight(Reftex)
	CameraProjMode fxcam,1
	CameraProjMode cam,0
	PositionEntity fxcam,65535,65535,65535
	
    ScaleEntity mesh,10,10,10
    EntityFX mesh,1
	EntityColor mesh,255,255,255*Intencity#
    PositionEntity mesh,65535,65535,65535
	AlignToVector mesh, TFormedX(), TFormedY(), TFormedZ(),0
	
	ShowEntity mesh
	
	If Mode=1
		BlicSpr=CreateSprite()
		If Ref EntityTexture BlicSpr,Ref
			ScaleSprite BlicSpr,size#,size#
			SpriteViewMode BlicSpr,2
			EntityBlend BlicSpr,3
			EntityColor BlicSpr,255,255,255*IntencitySpr#
			EntityFX BlicSpr,17
			PositionEntity BlicSpr,65535,65535,65535
			MoveEntity BlicSpr, -10*TFormedX(), -10*TFormedY(), -10*TFormedZ()
			PointEntity BlicSpr, fxcam
			EntityOrder BlicSpr,-1
		EndIf
		
		tex_sz=TextureWidth(Reftex) 
		
		SetCubeFace Reftex,0 
		RotateEntity fxcam,0,90,0 
		RenderWorld 
		CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Reftex) 
		
		SetCubeFace Reftex,1 
		RotateEntity fxcam,0,0,0 
		RenderWorld 
		CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Reftex) 
		
		SetCubeFace Reftex,2 
		RotateEntity fxcam,0,-90,0 
		RenderWorld 
		CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Reftex) 
		
		SetCubeFace Reftex,3 
		RotateEntity fxcam,0,180,0 
		RenderWorld 
		CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Reftex) 
		
		SetCubeFace Reftex,4 
		RotateEntity fxcam,-90,0,0 
		RenderWorld 
		CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Reftex) 
		
		SetCubeFace Reftex,5 
		RotateEntity fxcam,90,0,0 
		RenderWorld 
		CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Reftex)
		
		If Mode=1
			FreeEntity BlicSpr
		EndIf
		HideEntity mesh
		CameraProjMode cam,1

		FreeEntity fxcam
End Function

;Global mesh = LoadMesh("mesh.3ds")

;PointEntity light,mesh ;Point the light towards the mesh. You dont have to do this just nice if you do.