------------------------------------------------------------------
-- 관리자 계정
------------------------------------------------------------------
-- nxcr 계정 생성
CREATE USER nxcr
IDENTIFIED BY nxcr
DEFAULT TABLESPACE USERS;

GRANT CONNECT, RESOURCE TO nxcr;


------------------------------------------------------------------
-- NXCR 계정
------------------------------------------------------------------
DROP TABLE TB_EMPL;
DROP TABLE TB_DEPT;
DROP TABLE TB_POS;

--부서테이블
CREATE TABLE TB_DEPT(
    DEPT_CD VARCHAR2(2) PRIMARY KEY, 
    DEPT_NAME VARCHAR2(50)
);

INSERT INTO TB_DEPT VALUES('01', 'Accounting Team');    
INSERT INTO TB_DEPT VALUES('02', 'HR Team');
INSERT INTO TB_DEPT VALUES('03', 'Sales Team');
INSERT INTO TB_DEPT VALUES('04', 'Design Team');
INSERT INTO TB_DEPT VALUES('05', 'Education Team');
    
--직급테이블
CREATE TABLE TB_POS(
    POS_CD VARCHAR2(2) PRIMARY KEY, 
    POS_NAME VARCHAR2(50)
);

INSERT INTO TB_POS VALUES('01', 'Chairman');
INSERT INTO TB_POS VALUES('02', 'Division Manager');
INSERT INTO TB_POS VALUES('03', 'Assistant Manager');
INSERT INTO TB_POS VALUES('04', 'Officer');
    


--사원테이블
--married컬럼은 VO에서는 boolean타입이지만, oracle type중 boolean이 없으므로, 0|1로 관리한다.
--jdbc api의 get|setBoolean 메소드가 이 변환을 지원한다. 
--java boolean <----> jdbc api <------> oracle number(1,0) 또는 char('1','0')
CREATE TABLE TB_EMPL(
    EMPL_ID VARCHAR2(5),
    FULL_NAME VARCHAR2(50), 
    DEPT_CD VARCHAR2(2), 
    POS_CD VARCHAR2(2), 
    HIRE_DATE DATE, 
    SALARY NUMBER(12),
    GENDER CHAR(1), 
    MARRIED NUMBER(1),  --true 1 | false 0
    MEMO LONG,
    CONSTRAINT PK_EMPL_ID PRIMARY KEY(EMPL_ID),
    CONSTRAINT FK_DEPT_CD FOREIGN KEY(DEPT_CD) REFERENCES TB_DEPT(DEPT_CD),
    CONSTRAINT FK_POS_CD FOREIGN KEY(POS_CD) REFERENCES TB_POS(POS_CD),
    CONSTRAINT CK_GENDER CHECK(GENDER IN ('M', 'W')),
    CONSTRAINT CK_MARRIED CHECK (MARRIED IN (1, 0))
);

INSERT INTO NXCR.TB_EMPL (EMPL_ID,FULL_NAME,DEPT_CD,POS_CD,HIRE_DATE,GENDER,MARRIED,SALARY,MEMO) VALUES ('AA001','Olivia','01','03',TO_DATE('03-OCT-10','DD-MON-RR'),'W',1,83000,'ivory
blue
red');
INSERT INTO NXCR.TB_EMPL (EMPL_ID,FULL_NAME,DEPT_CD,POS_CD,HIRE_DATE,GENDER,MARRIED,SALARY,MEMO) VALUES ('AA002','John','02','04',TO_DATE('11-OCT-05','DD-MON-RR'),'M',0,76000,'greenyellow');
INSERT INTO NXCR.TB_EMPL (EMPL_ID,FULL_NAME,DEPT_CD,POS_CD,HIRE_DATE,GENDER,MARRIED,SALARY,MEMO) VALUES ('BB001','Jackson','03','03',TO_DATE('06-FEB-07','DD-MON-RR'),'M',1,95000,'aliceblue');
INSERT INTO NXCR.TB_EMPL (EMPL_ID,FULL_NAME,DEPT_CD,POS_CD,HIRE_DATE,GENDER,MARRIED,SALARY,MEMO) VALUES ('BB002','Maia','04','02',TO_DATE('12-MAY-09','DD-MON-RR'),'W',0,60000,'ivory');
INSERT INTO NXCR.TB_EMPL (EMPL_ID,FULL_NAME,DEPT_CD,POS_CD,HIRE_DATE,GENDER,MARRIED,SALARY,MEMO) VALUES ('CC001','Adam','01','04',TO_DATE('09-JAN-01','DD-MON-RR'),'M',1,88000,'greenyellow');
INSERT INTO NXCR.TB_EMPL (EMPL_ID,FULL_NAME,DEPT_CD,POS_CD,HIRE_DATE,GENDER,MARRIED,SALARY,MEMO) VALUES ('DD001','Ray','02','03',TO_DATE('02-FEB-16','DD-MON-RR'),'M',1,60000,'lightpink');


--조회
select * from tb_empl;
select * from tb_dept;
select * from tb_pos;

SELECT * FROM TB_EMPL WHERE FULL_NAME like  ORDER BY EMPL_ID;




--트랜잭션 처리 commit
commit;



--======================================
-- movie테이블
--======================================
drop table tb_movie;
create table tb_movie(
    id varchar2(10) primary key,
    title varchar2(300) not null,
    poster varchar2(300),
    outline long
);


select * from tb_movie;

insert into tb_movie values('movie01','imagerc::movies/aladin.jpg','알라딘','1992년에 개봉한 디즈니 애니메이션 알라딘을 원작으로 하는 실사 영화. 가이 리치가 연출을 맡았으며 알라딘 역에 메나 마수드, 자스민 역에 나오미 스콧, 지니 역에 윌 스미스가 캐스팅 되었다.');
insert into tb_movie values('movie02','imagerc::movies/man_in_black.jpg','맨인블랙','배리 소넨필드 감독, 토미 리 존스[1], 윌 스미스 주연, 스티븐 스필버그 제작(제작사가 스필버그가 세운 엠블린 엔터테인먼트 영화사), 소니 픽쳐스 엔터테인먼트 배급인 시리즈 영화.');
insert into tb_movie values('movie03','imagerc::movies/hancock.png','핸콕','Hancock. 영어권의 이름 혹은 성씨. 국립국어원의 규정 용례는 "행콕"이다.');
insert into tb_movie values('movie04','imagerc::movies/i_am_legend.jpg','나는전설이다','가장 최근작이자 처음으로 원제인 "나는 전설이다"를 그대로 사용한 2007년의 영화에서 무대는 뉴욕으로 바뀐다.');
insert into tb_movie values('movie05','imagerc::movies/mr_hitch.jpg','Mr.히치','히치(윌 스미스:Will Smith)는 거금을 받고 비밀리에 남자들의 데이트를 도와주는 성공률 100%의 전설적인 데이트 코치. 매혹적인 스타 알레그라 콜에 반한 연애경험 0%의 회계사 알버트를 도와주던 히치는, 오히려 알레그라 콜의 행적을 쫓는 뉴욕 최고의 스캔들 전문 기자 사라에게 첫눈에 빠져 버리게 된다. 하지만 명성 자자한 연애학 박사 히치도 그녀 앞에만 서면 실수 투성이, 툭하면 데이트를 망쳐 버리기 일쑤인데… 과연, 그는 자신의 데이트에서도 성공할 수 있을까?');

commit;

