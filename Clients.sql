Create database Clients


Create table ClientInfo
(
 Username varchar(100),
 Password varchar(100)
 primary key(username)

)

Select * from ClientInfo

create procedure signin
@usrname Varchar(20),
@passw varchar(20),
@flag int output
As
Begin
if Exists(Select ClientInfo.Username from ClientInfo where ClientInfo.username=@usrname and ClientInfo.Password=@passw)
begin
set @flag=1
end
else
begin
 set @flag=0
end
End
create procedure checkusername
@usrname Varchar(20),
@flag int output
As
Begin
if Exists(Select ClientInfo.Username from ClientInfo where ClientInfo.username=@usrname)
begin
set @flag=1
end
else
begin
 set @flag=0
end
End