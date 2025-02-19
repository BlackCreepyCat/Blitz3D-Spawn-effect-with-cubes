Type block
Field x#
Field y#
Field r#
Field g#
Field b#
Field entity
End Type

Function LoadImageAsTween(file$)
tweenimg=LoadImage (file$)
If tweenimg=0 Then
RuntimeError "Image not found"
Else
     If ImageWidth(tweenimg)>64 Then
          RuntimeError "Image too large! This would probably crash your PC..."
     ElseIf ImageHeight(tweenimg)>64 Then
          RuntimeError "Image too large! This would probably crash your PC..."
     EndIf
SetBuffer ImageBuffer(tweenimg)
For x# = 0 To ImageWidth(tweenimg)-1 ;Step 2
For y# = 0 To ImageHeight(tweenimg)-1 ;Step 2
GetColor x#,y#
red#=ColorRed()
green#=ColorGreen()
blue#=ColorBlue()
all.block = New block
all\x#=x#;/2
all\y#=-y#;(y#/2)
all\r#=red#
all\g#=green#
all\b#=blue#
Next
Next
SetBuffer BackBuffer()
EndIf

imgw=ImageWidth(tweenimg)-1
imgh=ImageHeight(tweenimg)-1

End Function

Function SaveCurrentRender(file$,frameskip,force=False)
If r Mod frameskip=0
rtempimg=CreateImage (320,240)
     CopyRect 0,0,320,240,0,0,FrontBuffer,ImageBuffer(rtempimg)
     ver=SaveImage (rtempimg,file$)
     If ver=0 Then 
     DebugLog "Error in saving file "+file$
     EndIf
ElseIf force=True
rtempimg=CreateImage (320,240)
     CopyRect 0,0,320,240,0,0,FrontBuffer,ImageBuffer(rtempimg)
     ver=SaveImage (rtempimg,file$)
     If ver=0 Then 
     DebugLog "Error in saving forced file "+file$
     EndIf
EndIf

r=r+1

End Function

Function SetTween(blockalpha#,scale#)
For all.block=Each block
tempblock=CreateCube()
all\entity=tempblock
EntityColor tempblock,all\r,all\g,all\b
EntityAlpha tempblock,blockalpha#
PositionEntity tempblock,Rnd(-200,200),Rnd(-200,200),Rnd(-200,200)
RotateEntity tempblock,Rnd(360),Rnd(360),Rnd(360)
Next

CaptureWorld

For all.block=Each block
PositionEntity all\entity,all\x*2*scale,all\y*2*scale,0
RotateEntity all\entity,0,0,0
Next
End Function

Function RenderTween();Use instead of RenderWorld()
RenderWorld(Log(tween#))
tween#=tween#+tweeninc#

tweeninc=tweeninc#-0.000018
If Log(tween#)>1 Then
UpdateWorld
RenderWorld(1)
Flip
;savecurrentrender("C:/logobmp/logo"+r+".bmp",4,True) <--Remove the semicolon from the start of this line and the similar one below to activate saving.
Color 150,150,150
Text 0,0,"Render Completed. Press a Mouse Button."
Flip
WaitMouse()
End
EndIf
End Function



;**************************************  EXAMPLE - READ THIS FIRST  **************************************

;Example Usage - run LoadImageAsTween with the filename of a 32 x 32 pixel Image, then 'arm' the tween with SetTween.
;Play with the scale and opacity values for a bit of fun ;-). Also make sure to include the global variables.
;Finally replace the usual RenderWorld call with RenderTween, which does some lovely logarithmic stuff. Have Fun!!

;New in this version - Remove the semicolons from the SaveCurrentRender function calls to save a string of BMPs into C:\logobmp\.
;These can be used to make animated gifs etc. Image sizes other than 32x32 are supported. Don't use images OVER 32x32, though.


Global imgh;These are used to set camera position.
Global imgw;  "    "    "   "  "     "       "  (I'm lazy ^.^)

Graphics3D 1024,768,32,2 ;This small size is purely for saving, but if you're not going to save, up this to 1024,768...
camera=CreateCamera()
;CameraClsColor camera,255,255,255;This makes a white background, which looks better with some images.

light=CreateLight()
PositionEntity light,30,-30,-40
Cls

LoadImageAsTween("image.jpg");Up to 32 x 32 Pixels ONLY. It's actually quite a bit when you think about some NES sprites...
SetTween(0.8,1)
Global tween#=0;This line must be in the code
Global tweeninc#=0.01;As must this one...
Global r = -1;Oh - and this one too

PositionEntity camera,imgw,-imgh,-60

While Not KeyDown(1)
UpdateWorld
RenderTween()
Flip
;savecurrentrender("C:/logobmp/logo"+r+".bmp",2) <-- The similar one below.
Wend
;~IDEal Editor Parameters:
;~C#Blitz3D