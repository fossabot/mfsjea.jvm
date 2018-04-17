package io.github.ranolp.mfsjea

import java.text.Normalizer
import java.util.regex.Pattern

object Mfsjea {
    private data class AlphabetKeyboard(val name: String, val layout: String)
    private data class CombinationTable(
        val cho: Map<String, Char> = emptyMap(),
        val jung: Map<String, Char> = emptyMap(),
        val jong: Map<String, Char> = emptyMap()
    )

    private data class HangulKeyboard(
        val name: String,
        val layout: String,
        val combinationTable: CombinationTable,
        val beol: Int
    )

    data class Result(
        val alphabetName: String,
        val hangulName: String,
        val str: String,
        val count: Int,
        val score: Int
    ) {
        val name = "$alphabetName - $hangulName"
    }

    private val REGEX_2350 =
        "[가-각간갇-갊감-갗같-객갠갤갬-갭갯-갱갸-갹갼걀걋걍걔걘걜거-걱건걷-걸걺검-겁것-겆겉-게겐겔겜-겝겟-겡겨-겪견겯-결겸-겹겻-경곁계곈곌곕곗고-곡곤곧-골곪곬곯-곱곳공-곶과-곽관괄괆괌-괍괏광괘괜괠괩괬-괭괴-괵괸괼굄-굅굇굉교굔굘굡굣구-국군굳-굶굻-굽굿궁-궂궈-궉권궐궜-궝궤궷귀-귁귄귈귐-귑귓규균귤그-극근귿-긁금-급긋긍긔기-긱긴긷-길긺김-깁깃깅-깆깊까-깎깐깔깖깜-깝깟-깡깥깨-깩깬깰깸-깹깻-깽꺄-꺅꺌꺼-꺾껀껄껌-껍껏-껑께-껙껜껨껫껭껴껸껼꼇-꼈꼍꼐꼬-꼭꼰꼲꼴꼼-꼽꼿꽁-꽃꽈-꽉꽐꽜-꽝꽤-꽥꽹꾀꾄꾈꾐-꾑꾕꾜꾸-꾹꾼꿀꿇-꿉꿋꿍-꿎꿔꿜꿨-꿩꿰-꿱꿴꿸뀀-뀁뀄뀌뀐뀔뀜-뀝뀨끄-끅끈끊끌끎끓-끕끗끙끝끼-끽낀낄낌-낍낏낑나-낚난낟-낢남-납낫-낯낱낳-낵낸낼냄-냅냇-냉냐-냑냔냘냠냥너-넉넋-넌널넒-넓넘-넙넛-넝넣-넥넨넬넴-넵넷-넹녀-녁년녈념-녑녔-녕녘녜녠노-녹논놀놂놈-놉놋농높-놔놘놜놨뇌뇐뇔뇜-뇝뇟뇨-뇩뇬뇰뇹뇻뇽누-눅눈눋-눌눔-눕눗눙눠눴눼뉘뉜뉠뉨-뉩뉴-뉵뉼늄-늅늉느-늑는늘-늚늠-늡늣능-늦늪늬늰늴니-닉닌닐닒님-닙닛닝닢다-닦단닫-닯닳-답닷-닻닿-댁댄댈댐-댑댓-댕댜더-덖던덛-덜덞-덟덤-덥덧덩덫덮데-덱덴델뎀-뎁뎃-뎅뎌뎐뎔뎠-뎡뎨뎬도-독돈돋-돌돎돐돔-돕돗동돛돝돠돤돨돼됐되된될됨-됩됫됴두-둑둔둘둠-둡둣둥둬뒀뒈뒝뒤뒨뒬뒵뒷뒹듀듄듈듐듕드-득든듣-들듦듬-듭듯등듸디-딕딘딛-딜딤-딥딧-딪따-딱딴딸땀-땁땃-땅땋-땍땐땔땜-땝땟-땡떠-떡떤떨떪-떫떰-떱떳-떵떻-떽뗀뗄뗌-뗍뗏-뗑뗘뗬또-똑똔똘똥똬똴뙈뙤뙨뚜-뚝뚠뚤뚫-뚬뚱뛔뛰뛴뛸뜀-뜁뜅뜨-뜩뜬뜯-뜰뜸-뜹뜻띄띈띌띔-띕띠띤띨띰-띱띳띵라-락란랄람-랍랏-랒랖-랙랜랠램-랩랫-랭랴-략랸럇량러-럭런럴럼-럽럿-렁렇-렉렌렐렘-렙렛렝려-력련렬렴-렵렷-령례롄롑롓로-록론롤롬-롭롯롱롸롼뢍뢨뢰뢴뢸룀-룁룃룅료룐룔룝룟룡루-룩룬룰룸-룹룻룽뤄뤘뤠뤼-뤽륀륄륌륏륑류-륙륜률륨-륩륫륭르-륵른를름-릅릇릉-릊릍-릎리-릭린릴림-립릿링마-막만많-맒맘-맙맛망-맞맡맣-맥맨맬맴-맵맷-맺먀-먁먈먕머-먹먼멀멂멈-멉멋멍-멎멓-멕멘멜멤-멥멧-멩며-멱면멸몃-명몇몌모-목몫-몬몰몲몸-몹못몽뫄뫈뫘-뫙뫼묀묄묍묏묑묘묜묠묩묫무-묶문묻-묾뭄-뭅뭇뭉뭍뭏-뭐뭔뭘뭡뭣뭬뮈뮌뮐뮤뮨뮬뮴뮷므믄믈믐믓미-믹민믿-밀밂밈-밉밋-밍및밑바-반받-밟밤-밥밧방밭배-백밴밸뱀-뱁뱃-뱅뱉뱌-뱍뱐뱝버-벅번벋-벌벎범-법벗벙-벚베-벡벤벧-벨벰-벱벳-벵벼-벽변별볍볏-병볕볘볜보-볶본볼봄-봅봇봉봐봔봤봬뵀뵈-뵉뵌뵐뵘-뵙뵤뵨부-북분붇-붊붐-붑붓붕붙-붚붜붤붰붸뷔-뷕뷘뷜뷩뷰뷴뷸븀븃븅브-븍븐블븜-븝븟비-빅빈빌빎빔-빕빗빙-빛빠-빡빤빨빪빰-빱빳-빵빻-빽뺀뺄뺌-뺍뺏-뺑뺘-뺙뺨뻐-뻑뻔뻗-뻘뻠뻣-뻥뻬뼁뼈-뼉뼘-뼙뼛-뼝뽀-뽁뽄뽈뽐-뽑뽕뾔뾰뿅뿌-뿍뿐뿔뿜뿟뿡쀼쁑쁘쁜쁠쁨-쁩삐-삑삔삘삠-삡삣삥사-삭삯-산삳-삶삼-삽삿-상샅새-색샌샐샘-샙샛-생샤-샥샨샬샴-샵샷샹섀섄섈섐섕서-선섣-설섦-섧섬-섭섯-성섶세-섹센셀셈-셉셋-셍셔-셕션셜셤-셥셧-셩셰셴셸솅소-솎손솔솖솜-솝솟송솥솨-솩솬솰솽쇄쇈쇌쇔쇗-쇘쇠쇤쇨쇰-쇱쇳쇼-쇽숀숄숌-숍숏숑수-숙순숟-술숨-숩숫숭숯숱-숲숴쉈쉐-쉑쉔쉘쉠쉥쉬-쉭쉰쉴쉼-쉽쉿슁슈-슉슐슘슛슝스-슥슨슬-슭슴-습슷승시-식신싣-실싫-십싯싱싶싸-싹싻-싼쌀쌈-쌉쌌-쌍쌓-쌕쌘쌜쌤-쌥쌨-쌩썅써-썩썬썰썲썸-썹썼-썽쎄쎈쎌쏀쏘-쏙쏜쏟-쏠쏢쏨-쏩쏭쏴-쏵쏸쐈쐐쐤쐬쐰쐴쐼-쐽쑈쑤-쑥쑨쑬쑴-쑵쑹쒀쒔쒜쒸쒼쓩쓰-쓱쓴쓸쓺쓿-씁씌씐씔씜씨-씩씬씰씸-씹씻씽아-악안-않알-앎앓-압앗-앙앝-앞애-액앤앨앰-앱앳-앵야-약얀얄얇얌-얍얏양얕얗-얘얜얠얩어-억언-얹얻-얾엄-엊엌엎에-엑엔엘엠-엡엣엥여-엮연열엶-엷염-영옅-예옌옐옘-옙옛-옜오-옥온올-옮옰옳-옵옷옹옻와-왁완왈왐-왑왓-왕왜-왝왠왬왯왱외-왹왼욀욈-욉욋욍요-욕욘욜욤-욥욧용우-욱운울-욺움-웁웃웅워-웍원월웜-웝웠-웡웨-웩웬웰웸-웹웽위-윅윈윌윔-윕윗윙유-육윤율윰-윱윳융윷으-윽은을읊음-읍읏응-의읜읠읨읫이-익인일-읾잃-입잇-잊잎자-작잔잖-잘잚잠-잡잣-잦재-잭잰잴잼-잽잿-쟁쟈-쟉쟌쟎쟐쟘쟝쟤쟨쟬저-적전절젊점-접젓정-젖제-젝젠젤젬-젭젯젱져젼졀졈-졉졌-졍졔조-족존졸졺좀-좁좃종-좇좋-좍좔좝좟좡좨좼-좽죄죈죌죔-죕죗죙죠-죡죤죵주-죽준줄-줆줌-줍줏중줘줬줴쥐-쥑쥔쥘쥠-쥡쥣쥬쥰쥴쥼즈-즉즌즐즘-즙즛증지-직진짇-질짊짐-집짓징-짖짙-짚짜-짝짠짢짤짧짬-짭짯-짱째-짹짼쨀쨈-쨉쨋-쨍쨔쨘쨩쩌-쩍쩐쩔쩜-쩝쩟-쩡쩨쩽쪄쪘쪼-쪽쫀쫄쫌-쫍쫏쫑쫓쫘-쫙쫠쫬쫴쬈쬐쬔쬘쬠-쬡쭁쭈-쭉쭌쭐쭘-쭙쭝쭤쭸-쭹쮜쮸쯔쯤쯧쯩찌-찍찐찔찜-찝찡-찢찧-착찬찮찰참-찹찻-찾채-책챈챌챔-챕챗-챙챠챤챦챨챰챵처-척천철첨-첩첫-청체-첵첸첼쳄-쳅쳇쳉쳐쳔쳤쳬쳰촁초-촉촌촐촘-촙촛총촤촨촬촹최쵠쵤쵬-쵭쵯쵱쵸춈추-축춘출춤-춥춧충춰췄췌췐취췬췰췸-췹췻췽츄츈츌츔츙츠-측츤츨츰-츱츳층치-칙친칟-칡침-칩칫칭카-칵칸칼캄-캅캇캉캐-캑캔캘캠-캡캣-캥캬-캭컁커-컥컨컫-컬컴-컵컷-컹케-켁켄켈켐-켑켓켕켜켠켤켬-켭켯-켱켸코-콕콘콜콤-콥콧콩콰-콱콴콸쾀쾅쾌쾡쾨쾰쿄쿠-쿡쿤쿨쿰-쿱쿳쿵쿼퀀퀄퀑퀘퀭퀴-퀵퀸퀼큄-큅큇큉큐큔큘큠크-큭큰클큼-큽킁키-킥킨킬킴-킵킷킹타-탁탄탈-탉탐-탑탓-탕태-택탠탤탬-탭탯-탱탸턍터-턱턴털턺텀-텁텃-텅테-텍텐텔템-텝텟텡텨텬텼톄톈토-톡톤톨톰-톱톳통톺톼퇀퇘퇴퇸툇툉툐투-툭툰툴툼-툽툿퉁퉈퉜퉤튀-튁튄튈튐-튑튕튜튠튤튬튱트-특튼튿-틀틂틈-틉틋틔틘틜틤-틥티-틱틴틸팀-팁팃팅파-팎판팔팖팜-팝팟-팡팥패-팩팬팰팸-팹팻-팽퍄-퍅퍼-퍽펀펄펌-펍펏-펑페-펙펜펠펨-펩펫펭펴편펼폄-폅폈-평폐폘폡폣포-폭폰폴폼-폽폿퐁퐈퐝푀푄표푠푤푭푯푸-푹푼푿-풀풂품-풉풋풍풔풩퓌퓐퓔퓜퓟퓨퓬퓰퓸퓻퓽프픈플픔-픕픗피-픽핀필핌-핍핏핑하-학한할핥함-합핫항해-핵핸핼햄-햅햇-행햐향허-헉헌헐헒험-헙헛헝헤-헥헨헬헴-헵헷헹혀-혁현혈혐-협혓-형혜혠혤혭호-혹혼홀홅홈-홉홋홍홑화-확환활홧황홰-홱홴횃횅회-획횐횔횝횟횡효횬횰횹횻후-훅훈훌훑훔훗훙훠훤훨훰훵훼-훽휀휄휑휘-휙휜휠휨-휩휫휭휴-휵휸휼흄흇흉흐-흑흔흖-흙흠-흡흣흥흩희흰흴흼-흽힁히-힉힌힐힘-힙힛힝]".toPattern()

    private val REGEX_NUMBERS = "[0-9,.+\\-*\\/%]{2,}".toPattern()
    private val REGEX_PARENTHESIS = "\\(.+\\)".toPattern()

    private const val STD_CHO = "ᄀᄁᄂᄃᄄᄅᄆᄇᄈᄉᄊᄋᄌᄍᄎᄏᄐᄑᄒ"
    private const val STD_JUNG = "ᅡᅢᅣᅤᅥᅦᅧᅨᅩᅪᅫᅬᅭᅮᅯᅰᅱᅲᅳᅴᅵ"
    private const val STD_JONG = "ᆨᆩᆪᆫᆬᆭᆮᆯᆰᆱᆲᆳᆴᆵᆶᆷᆸᆹᆺᆻᆼᆽᆾᆿᇀᇁᇂ"

    private const val COMPAT_CHO = "ㄱㄲㄳㄴㄵㄶㄷㄸㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅃㅄㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ"
    private const val COMPAT_JUNG = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ"

    private const val CONVERT_CHO = "ᄀᄁ ᄂ  ᄃᄄᄅ       ᄆᄇᄈ ᄉᄊᄋᄌᄍᄎᄏᄐᄑᄒ"
    private const val CONVERT_JONG = "ᆨᆩᆪᆫᆬᆭᆮ ᆯᆰᆱᆲᆳᆴᆵᆶᆷᆸ ᆹᆺᆻᆼᆽ ᆾᆿᇀᇁᇂ"

    private val HANGUL_SYLLABLE_3 = "([ᄀ-ᄒ]+)([ᅡ-ᅵ]+)([ᆨ-ᇂ]*)".toPattern()
    private val HANGUL_SYLLABLE_2 = "([ㄱ-ㅎ])([ㅏ-ㅣ][ㅏ-ㅣ]?)([ㄱ-ㅎ]?[ㄱ-ㅎ]?)(?![ㅏ-ㅣ])".toPattern()

    private const val LAYOUT_ALPHABET_QWERTY =
        " `~1!2@3#4$5%6^7&8*9(0)-_=+\\|qQwWeErRtTyYuUiIoOpP[{]}aAsSdDfFgGhHjJkKlL;:\'\"zZxXcCvVbBnNmM,<.>/?"
    private const val LAYOUT_ALPHABET_DVORAK =
        " `~1!2@3#4$5%6^7&8*9(0)[{]}\\|\'\",<.>pPyYfFgGcCrRlL/?=+aAoOeEuUiIdDhHtTnNsS-_;:qQjJkKxXbBmMwWvVzZ"
    private const val LAYOUT_ALPHABET_COLEMAK =
        " `~1!2@3#4$5%6^7&8*9(0)-_=+\\|qQwWfFpPgGjJlLuUyY;:[{]}aArRsStTdDhHnNeEiIoO\'\"zZxXcCvVbBkKmM,<.>/?"

    private val ALPHABET_QWERTY = AlphabetKeyboard("Qwerty", LAYOUT_ALPHABET_QWERTY)
    private val ALPHABET_DVORAK = AlphabetKeyboard("Dvorak", LAYOUT_ALPHABET_DVORAK)
    private val ALPHABET_COLEMAK = AlphabetKeyboard("Colemak", LAYOUT_ALPHABET_COLEMAK)

    private val ALPHABET_LAYOUTS = listOf(ALPHABET_QWERTY, ALPHABET_DVORAK, ALPHABET_COLEMAK)

    private const val LAYOUT_DUBEOL_STANDARD =
        " `~1!2@3#4$5%6^7&8*9(0)-_=+\\|ㅂㅃㅈㅉㄷㄸㄱㄲㅅㅆㅛㅛㅕㅕㅑㅑㅐㅒㅔㅖ[{]}ㅁㅁㄴㄴㅇㅇㄹㄹㅎㅎㅗㅗㅓㅓㅏㅏㅣㅣ;:\'\"ㅋㅋㅌㅌㅊㅊㅍㅍㅠㅠㅜㅜㅡㅡ,<.>/?"

    private const val LAYOUT_SEBEOL_390 =
        " `~ᇂᆽᆻ@ᆸ#ᅭ\$ᅲ%ᅣ^ᅨ&ᅴ*ᅮ(ᄏ)-_=+\\|ᆺᇁᆯᇀᅧᆿᅢᅤᅥ;ᄅ<ᄃ7ᄆ8ᄎ9ᄑ>[{]}ᆼᆮᆫᆭᅵᆰᅡᆩᅳ/ᄂ\'ᄋ4ᄀ5ᄌ6ᄇ:ᄐ\"ᆷᆾᆨᆹᅦᆱᅩᆶᅮ!ᄉ0ᄒ1,2.3ᅩ?"
    private const val LAYOUT_SEBEOL_FINAL =
        " *※ᇂᆩᆻᆰᆸᆽᅭᆵᅲᆴᅣ=ᅨ“ᅴ”ᅮ\'ᄏ~);>+:\\ᆺᇁᆯᇀᅧᆬᅢᆶᅥᆳᄅ5ᄃ6ᄆ7ᄎ8ᄑ9(%</ᆼᆮᆫᆭᅵᆲᅡᆱᅳᅤᄂ0ᄋ1ᄀ2ᄌ3ᄇ4ᄐ·ᆷᆾᆨᆹᅦᆿᅩᆪᅮ?ᄉ-ᄒ\",,..ᅩ!"

    private val COMB_DUBEOL_STANDARD =
        CombinationTable(
            jung = mapOf(
                "ㅗㅏ" to 'ㅘ',
                "ㅗㅐ" to 'ㅙ',
                "ㅗㅣ" to 'ㅚ',
                "ㅜㅓ" to 'ㅝ',
                "ㅜㅔ" to 'ㅞ',
                "ㅜㅣ" to 'ㅟ',
                "ㅡㅣ" to 'ㅢ'
            ), jong = mapOf(
                "ㄱㅅ" to 'ㄳ',
                "ㄴㅈ" to 'ㄵ',
                "ㄴㅎ" to 'ㄶ',
                "ㄹㄱ" to 'ㄺ',
                "ㄹㅁ" to 'ㄻ',
                "ㄹㅂ" to 'ㄼ',
                "ㄹㅅ" to 'ㄽ',
                "ㄹㅌ" to 'ㄾ',
                "ㄹㅍ" to 'ㄿ',
                "ㄹㅎ" to 'ㅀ',
                "ㅂㅅ" to 'ㅄ'
            )
        )

    private val COMB_SEBEOL_390 =
        CombinationTable(
            mapOf(
                "ᄀᄀ" to 'ᄁ',
                "ᄃᄃ" to 'ᄄ',
                "ᄇᄇ" to 'ᄈ',
                "ᄉᄉ" to 'ᄊ',
                "ᄌᄌ" to 'ᄍ'
            ),
            mapOf(
                "ᅩᅡ" to 'ᅪ',
                "ᅩᅢ" to 'ᅫ',
                "ᅩᅵ" to 'ᅬ',
                "ᅮᅥ" to 'ᅯ',
                "ᅮᅦ" to 'ᅰ',
                "ᅮᅵ" to 'ᅱ',
                "ᅳᅵ" to 'ᅴ'
            ), mapOf(
                "ᆨᆨ" to 'ᆩ',
                "ᆨᆺ" to 'ᆪ',
                "ᆫᆽ" to 'ᆬ',
                "ᆫᇂ" to 'ᆭ',
                "ᆯᆨ" to 'ᆰ',
                "ᆯᆷ" to 'ᆱ',
                "ᆯᆸ" to 'ᆲ',
                "ᆯᆺ" to 'ᆳ',
                "ᆯᇀ" to 'ᆴ',
                "ᆯᇁ" to 'ᆵ',
                "ᆯᇂ" to 'ᆶ',
                "ᆸᆺ" to 'ᆹ',
                "ᆺᆺ" to 'ᆻ'
            )
        )
    private val COMB_SEBEOL_FINAL =
        CombinationTable(
            mapOf(
                "ᄀᄀ" to 'ᄁ',
                "ᄃᄃ" to 'ᄄ',
                "ᄇᄇ" to 'ᄈ',
                "ᄉᄉ" to 'ᄊ',
                "ᄌᄌ" to 'ᄍ'
            ),
            mapOf(
                "ᅩᅡ" to 'ᅪ',
                "ᅩᅢ" to 'ᅫ',
                "ᅩᅵ" to 'ᅬ',
                "ᅮᅥ" to 'ᅯ',
                "ᅮᅦ" to 'ᅰ',
                "ᅮᅵ" to 'ᅱ',
                "ᅳᅵ" to 'ᅴ'
            )
        ) // STRICT mode.

    private val SEBEOL_390 = HangulKeyboard("세벌식 390", LAYOUT_SEBEOL_390, COMB_SEBEOL_390, 3)
    private val SEBEOL_FINAL = HangulKeyboard("세벌식 최종", LAYOUT_SEBEOL_FINAL, COMB_SEBEOL_FINAL, 3)
    private val DUBEOL_STANDARD = HangulKeyboard("두벌식 표준", LAYOUT_DUBEOL_STANDARD, COMB_DUBEOL_STANDARD, 2)

    private val HANGUL_LAYOUTS = listOf(DUBEOL_STANDARD, SEBEOL_390, SEBEOL_FINAL)

    private fun countRegex(s: String, regex: Pattern): Int {
        val matcher = regex.matcher(s)
        var result = 0
        while (matcher.find()) {
            result += matcher.group().length
        }
        return result
    }

    private fun count2350(s: String): Int = countRegex(s, REGEX_2350)

    private fun countNumbers(s: String): Int {
        val matcher = REGEX_NUMBERS.matcher(s)
        var score = 0
        while (matcher.find()) {
            score += matcher.group().length
        }
        return score
    }

    private fun convert(str: String, from: String, to: String): String =
        str.map { c ->
            from.indexOf(c).let { if (it > 0 && it < to.length) to[it] else c }
        }.joinToString("")


    private fun convertCompatibleCho(c: String): String =
        c.map { CONVERT_CHO[COMPAT_CHO.indexOf(it)] }.joinToString("")

    private fun convertCompatibleJung(c: String) =
        c.map { STD_JUNG[COMPAT_JUNG.indexOf(it)] }.joinToString("")

    private fun convertCompatibleJong(c: String) =
        if (c.isEmpty()) ""
        else c.map { CONVERT_JONG[COMPAT_CHO.indexOf(it)] }.joinToString("")

    private fun dudgks(str: String, fr: AlphabetKeyboard, to: HangulKeyboard): String {
        val converted = convert(str, fr.layout, to.layout)
        val matcher = HANGUL_SYLLABLE_2.matcher(converted)

        val result = StringBuilder()
        var last = 0

        while (matcher.find()) {
            if (last < matcher.start()) {
                result.append(converted.substring(last, matcher.start()))
            }
            last = matcher.end()

            val cho = matcher.group(1)
            val jung = matcher.group(2)
            val jong = matcher.group(3)

            val convertedCho = convertCompatibleCho(to.combinationTable.cho[cho]?.toString() ?: cho)
            val convertedJung = convertCompatibleJung(to.combinationTable.jung[jung]?.toString() ?: jung)
            val convertedJong = convertCompatibleJong(to.combinationTable.jong[jong]?.toString() ?: jong)

            result.append(Normalizer.normalize(convertedCho + convertedJung + convertedJong, Normalizer.Form.NFC))
        }

        if (last < str.length) {
            result.append(converted.substring(last))
        }

        return result.toString()
    }

    private fun jeamfs(str: String, fr: AlphabetKeyboard, to: HangulKeyboard): String {
        val converted = convert(str, fr.layout, to.layout)
        val matcher = HANGUL_SYLLABLE_3.matcher(converted)

        val result = StringBuilder()
        var last = 0

        while (matcher.find()) {
            if (last < matcher.start()) {
                result.append(converted.substring(last, matcher.start()))
            }
            last = matcher.end()

            val cho = matcher.group(1)
            val jung = matcher.group(2)
            val jong = matcher.group(3)

            val convertedCho = to.combinationTable.cho[cho]?.toString() ?: cho
            val convertedJung = to.combinationTable.jung[jung]?.toString() ?: jung
            val convertedJong = to.combinationTable.jong[jong]?.toString() ?: jong

            result.append(Normalizer.normalize(convertedCho + convertedJung + convertedJong, Normalizer.Form.NFC))
        }

        if (last < str.length) {
            result.append(converted.substring(last))
        }

        return result.toString()
    }

    private fun jeamfsORdudgks(str: String, fr: AlphabetKeyboard, to: HangulKeyboard): String =
        if (to.beol == 3) jeamfs(str, fr, to) else dudgks(str, fr, to)

    fun jeamfsList(str: String): List<Result> {
        val results = mutableListOf<Mfsjea.Result>()
        ALPHABET_LAYOUTS.forEach { alphabet ->
            HANGUL_LAYOUTS.forEach { hangul ->
                val result = jeamfsORdudgks(str, alphabet, hangul)
                results += Result(
                    alphabet.name,
                    hangul.name,
                    result,
                    count2350(result),
                    count2350(result) + countNumbers(result) + countRegex(result, REGEX_PARENTHESIS) * 10
                )
            }
        }
        return results.sortedByDescending { it.score }
    }

    fun jeamfsAuto(str: String): Result = jeamfsList(str).first()
}
