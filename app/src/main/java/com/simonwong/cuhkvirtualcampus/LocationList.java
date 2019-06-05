package com.simonwong.cuhkvirtualcampus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonwong on 13/10/2016.
 */

public class LocationList {
    private List<Location> Locationlist = new ArrayList<Location>();

    public LocationList(){
        this.addLocation();
    }

    public LocationList(String bus){
        this.addBusLocation();
    }

    public LocationList(int photo){ this.addLocationPhoto();}

    private void addLocation(){
        /*
        this.Locationlist.add(new Location("1", "MTR station", "火車站", "MTR", 22.413505, 114.210140));
        this.Locationlist.add(new Location("3", "Yasumoto International Academic Park", "康本", "YIA", 22.416216, 114.210935));
        */
        this.Locationlist.add(new Location("0", "Cheng Yu Tung Building", "鄭裕彤樓", "CYT", 22.412462, 114.210433, 1));
        this.Locationlist.add(new Location("1", "MTR Station", "港鐵站", "MTR", 22.413766, 114.209802, 1));
        this.Locationlist.add(new Location("2", "Esther Lee Building", "利黃瑤璧樓", "ELB", 22.414255, 114.208451, 1));
        this.Locationlist.add(new Location("3", "Yasumoto International Academic Park", "康本國際學術園", "YIA", 22.416051, 114.210907, 1));
        this.Locationlist.add(new Location("4", "Wu Ho Man Yuen Building", "伍何曼原樓", "WMY", 22.416891, 114.211708, 1));
        this.Locationlist.add(new Location("5", "An Integrated Teaching Building", "綜合教學大樓", "ATI", 22.416149, 114.211393, 1));
        this.Locationlist.add(new Location("6", "Ho Tim Building", "何添樓", "HTB", 22.415261, 114.207715, 1));
        this.Locationlist.add(new Location("7", "Wong Foo Yuan Building", "王福元樓", "FYB", 22.415138, 114.207516, 1));
        this.Locationlist.add(new Location("8", "Chen Kou Bun Building", "陳國本樓", "CKB", 22.415299, 114.207404, 1));
        this.Locationlist.add(new Location("9", "Sino Building", "信和樓", "SB", 22.415461, 114.207311, 1));
        this.Locationlist.add(new Location("10", "Hui Yeung Shing Building", "許讓成樓", "HYS", 22.414692, 114.207872, 1));
        this.Locationlist.add(new Location("11", "Orchid Lodge", "蘭苑", "OL Can", 22.415526, 114.207678, 1));
        this.Locationlist.add(new Location("12", "Chung Chi College Staff Club Clubhouse", "聯誼會", "CCCS", 22.415986, 114.207531, 1));
        this.Locationlist.add(new Location("13", "Li Wai Chun Building", "李慧珍樓", "LWCB", 22.415011, 114.207136, 1));
        this.Locationlist.add(new Location("14", "Chung Chi Tang", "眾志堂", "CC Can", 22.416511, 114.209789, 1));
        this.Locationlist.add(new Location("15", "Chung Chi College Chapel", "崇基教堂", "CCCC", 22.415952, 114.207189, 1));
        this.Locationlist.add(new Location("16", "Elisabeth Luce Moore Library", "牟路思怡圖書館", "CC Lib", 22.416375, 114.208772, 1));
        this.Locationlist.add(new Location("17", "Lake Ad Excellentiam", "未圓湖", "Lake", 22.416031, 114.209321, 1));
        this.Locationlist.add(new Location("18", "Pommerenke Student Centre", "龐萬倫學生中心禮堂", "PSC", 22.417105, 114.208838, 1));
        this.Locationlist.add(new Location("19", "Chung Chi Garden", "何草", "CCG", 22.417188, 114.211633, 1));
        this.Locationlist.add(new Location("20", "William M.W. Mong Engineering Building G/F", "蒙民偉工程學大樓地下", "ERB(G/F)", 22.418181, 114.207974, 2));
        this.Locationlist.add(new Location("21", "Ho Sin-Hang Engineering Building 5/F", "何善衡工程學大樓5樓", "SHB(5/F)", 22.418163, 114.207327, 2));
        this.Locationlist.add(new Location("22", "S.H. Ho College", "善衡書院", "SHHO", 22.418101, 114.210258, 1));
        this.Locationlist.add(new Location("23", "Morningside College", "晨興書院", "MC", 22.418864, 114.210307, 1));
        this.Locationlist.add(new Location("24", "Pentecostal Mission Hall Complex(Low Block)", "五旬節會樓低座", "PMHCL", 22.418805, 114.209715, 1));
        this.Locationlist.add(new Location("25", "University Health Centre", "大學保健醫療中心", "UHC", 22.419465, 114.210233, 1));
        this.Locationlist.add(new Location("26", "Mong Man Wai Building", "蒙民偉樓", "MMW", 22.419675, 114.209239, 2));
        this.Locationlist.add(new Location("27", "Choh-Ming Li Basic Medical Science Building", "李卓敏基本醫學大樓", "BMSB", 22.419601, 114.208685, 2));
        this.Locationlist.add(new Location("28", "Science Centre North Block", "科學館北座高錕樓", "SCN", 22.419784, 114.207981, 2));
        this.Locationlist.add(new Location("29", "Science Centre South Block", "科學館南座馬臨樓", "SCS", 22.419486, 114.207979, 2));
        this.Locationlist.add(new Location("30", "Science Centre East Block", "科學館東座", "SCE", 22.418831, 114.207754, 2));
        this.Locationlist.add(new Location("31", "The University Mall", "林蔭大道", "Mall", 22.419491, 114.206975, 2));
        this.Locationlist.add(new Location("32", "Lady Shaw Building", "邵逸夫夫人樓", "LSB", 22.418642, 114.206551, 2));
        this.Locationlist.add(new Location("33", "Pi Ch'iu Building", "碧秋樓", "PCB", 22.419618, 114.206381, 2));
        this.Locationlist.add(new Location("34", "Sui Loong Pao Building", "兆龍樓", "SLP", 22.419628, 114.205823, 2));
        this.Locationlist.add(new Location("35", "Y.C. Liang Hall", "潤昌堂", "LHC", 22.419901, 114.206538, 2));
        this.Locationlist.add(new Location("36", "Sir Run Run Shaw Hall", "邵逸夫堂", "SRR", 22.419908, 114.206991, 2));
        this.Locationlist.add(new Location("37", "Art Museum", "文物館", "AM", 22.419371, 114.206178, 2));
        this.Locationlist.add(new Location("38", "Institute of Chinese Studies", "中國文化研究所", "ICS", 22.419253, 114.205597, 2));
        this.Locationlist.add(new Location("39", "University Administration Building", "大學行政樓", "UAB", 22.419104, 114.205129, 2));
        this.Locationlist.add(new Location("40", "University Library", "大學圖書館", "U Lib", 22.419448, 114.205144, 2));
        this.Locationlist.add(new Location("41", "Lee Shau Kee Building", "李兆基樓", "LSK", 22.419681, 114.204076, 2));
        this.Locationlist.add(new Location("42", "Li Dak Sum Building", "李達三樓", "LDS", 22.419364, 114.204068, 2));
        this.Locationlist.add(new Location("43", "Fung King Hey Building", "馮景禧樓", "FKH", 22.419578, 114.203314, 2));
        this.Locationlist.add(new Location("44", "Leung Kau Kui Building", "梁銶琚樓", "KKB", 22.419998, 114.202837, 2));
        this.Locationlist.add(new Location("45", "John Fulton Centre", "富爾敦樓", "JFC", 22.418381, 114.204595, 2));
        this.Locationlist.add(new Location("46", "Park'n Shop Supermarket", "百佳超級市場", "Market", 22.418147, 114.204701, 2));
        this.Locationlist.add(new Location("47", "Swimming Pool", "游泳池", "Pool", 22.418081, 114.205306, 2));
        this.Locationlist.add(new Location("48", "Benjamin Franklin Centre", "范克廉樓", "BFC", 22.418181, 114.205202, 2));
        this.Locationlist.add(new Location("49", "Benjamin Franklin Centre Coffee Corner", "范克廉樓咖啡閣", "Coffee Con", 22.418353, 114.205563, 2));
        this.Locationlist.add(new Location("50", "Benjamin Franklin Centre Student Canteen", "范克廉樓學生膳堂", "Franklin", 22.418345, 114.205227, 2));
        this.Locationlist.add(new Location("51", "Women Workers' Cooperation", "女工合作社", "WWC", 22.418206, 114.205561, 2));
        this.Locationlist.add(new Location("52", "Cultural Square", "文化廣場", "CS", 22.418457, 114.205569, 2));
        this.Locationlist.add(new Location("53", "William M.W. Mong Engineering Building 9/F", "蒙民偉工程學大樓9樓", "ERB(9/F)", 22.418181, 114.207974, 2));
        this.Locationlist.add(new Location("54", "Staff Student Centre Leung Hung Kee Building", "樂群館梁雄姬樓", "LHKB", 22.421110, 114.209239, 3));
        this.Locationlist.add(new Location("55", "Ch'ien Mu Library", "錢穆圖書館", "NA Lib", 22.421460, 114.208331, 3));
        this.Locationlist.add(new Location("56", "Cheng Ming Building", "誠明館", "NAA", 22.421257, 114.207975, 3));
        this.Locationlist.add(new Location("57", "Humanities Building", "人文館", "NAH", 22.421628, 114.208171, 3));
        this.Locationlist.add(new Location("58", "Pavilion of Harmony", "合一亭", "POH", 22.421556, 114.209870, 3));
        this.Locationlist.add(new Location("59", "Cheung Chuk Shan Amenities Building", "張祝珊師生康樂大樓", "CCSAB", 22.421022, 114.205709, 3));
        this.Locationlist.add(new Location("60", "Wu Chung Multimedia Library", "胡忠多媒體圖書館", "UC Lib", 22.420897, 114.204617, 3));
        this.Locationlist.add(new Location("61", "Tsang Shiu Tim Building", "曾肇添樓", "UCA", 22.420548, 114.204841, 3));
        this.Locationlist.add(new Location("62", "T.C. Cheng Building", "鄭棟材樓", "UCC", 22.421150, 114.204852, 3));
        this.Locationlist.add(new Location("63", "Lee Woo Sing College", "和聲書院", "WS", 22.422352, 114.204334, 3));
        this.Locationlist.add(new Location("64", "Wu Yee Sun College", "伍宜孫書院", "WYS", 22.421983, 114.202554, 3));
        this.Locationlist.add(new Location("65", "Shaw College Lecture Theatre", "逸夫書院大講堂", "SWC", 22.422517, 114.201511, 3));
        this.Locationlist.add(new Location("66", "Wen Lan Tang", "文瀾堂", "WLS", 22.423174, 114.201739, 3));
        this.Locationlist.add(new Location("67", "Huen Wing Ming Building", "禤永明樓多功能學習中心", "HWM", 22.422738, 114.201931, 3));
        this.Locationlist.add(new Location("68", "Shaw College Gymnasium", "逸夫書院體育館", "Shaw gym", 22.422353, 114.200777, 3));
        this.Locationlist.add(new Location("69", "William M.W. Mong Engineering Building 4/F", "蒙民偉工程學大樓4樓", "ERB(4/F)", 22.418181, 114.207974, 2));
        this.Locationlist.add(new Location("70", "Ho Sin-Hang Engineering Building G/F", "何善衡工程學大樓地下", "SHB(G/F)", 22.418163, 114.207327, 2));
        this.Locationlist.add(new Location("71", "C.W. Chu College", "敬文書院", "CWC", 22.425401, 114.206614, 3));
        this.Locationlist.add(new Location("72", "Lo Kwee-Seong Integrated Biomedical Sciences Building", "羅桂祥綜合生物醫學大樓", "LKS", 22.427551, 114.204311, 3));
        this.Locationlist.add(new Location("73", "Jockey Club Postgraduate Hall", "賽馬會研究生宿舍", "JCPH",  22.420279, 114.212071, 2));
        this.Locationlist.add(new Location("74", "Residence 15", "十五苑", "R15",  22.423519, 114.206599, 3));
        this.Locationlist.add(new Location("75", "United College Staff Residence", "聯合苑", "UCSR", 22.423212, 114.205403, 3));
    }

    private void addBusLocation(){
        this.Locationlist.add(new Location("0", "Cheng Yu Tung Building", "鄭裕彤樓", "CYT", 22.412462, 114.210433, 1));
        this.Locationlist.add(new Location("1", "MTR Station", "港鐵站", "MTR", 22.413766, 114.209802, 1));
        this.Locationlist.add(new Location("2", "Esther Lee Building", "利黃瑤璧樓", "ELB", 22.414255, 114.208451, 1));
        this.Locationlist.add(new Location("3", "Yasumoto International Academic Park", "康本國際學術園", "YIA", 22.416051, 114.210907, 1));
        this.Locationlist.add(new Location("4", "Wu Ho Man Yuen Building", "伍何曼原樓", "WMY", 22.416891, 114.211708, 1));
        this.Locationlist.add(new Location("5", "An Integrated Teaching Building", "綜合教學大樓", "ATI", 22.416149, 114.211393, 1));
        this.Locationlist.add(new Location("6", "Ho Tim Building", "何添樓", "HTB", 22.415261, 114.207715, 1));
        this.Locationlist.add(new Location("7", "Wong Foo Yuan Building", "王福元樓", "FYB", 22.415138, 114.207516, 1));
        this.Locationlist.add(new Location("8", "Chen Kou Bun Building", "陳國本樓", "CKB", 22.415299, 114.207404, 1));
        this.Locationlist.add(new Location("9", "Sino Building", "信和樓", "SB", 22.415461, 114.207311, 1));
        this.Locationlist.add(new Location("10", "Hui Yeung Shing Building", "許讓成樓", "HYS", 22.414692, 114.207872, 1));
        this.Locationlist.add(new Location("11", "Orchid Lodge", "蘭苑", "OL Can", 22.415526, 114.207678, 1));
        this.Locationlist.add(new Location("12", "Chung Chi College Staff Club Clubhouse", "聯誼會", "CCCS", 22.415986, 114.207531, 1));
        this.Locationlist.add(new Location("13", "Li Wai Chun Building", "李慧珍樓", "LWCB", 22.415011, 114.207136, 1));
        this.Locationlist.add(new Location("14", "Chung Chi Tang", "眾志堂", "CC Can", 22.416511, 114.209789, 1));
        this.Locationlist.add(new Location("15", "Chung Chi College Chapel", "崇基教堂", "CCCC", 22.415952, 114.207189, 1));
        this.Locationlist.add(new Location("16", "Elisabeth Luce Moore Library", "牟路思怡圖書館", "CC Lib", 22.416375, 114.208772, 1));
        this.Locationlist.add(new Location("17", "Lake Ad Excellentiam", "未圓湖", "Lake", 22.416031, 114.209321, 1));
        this.Locationlist.add(new Location("18", "Pommerenke Student Centre", "龐萬倫學生中心禮堂", "PSC", 22.417105, 114.208838, 1));
        this.Locationlist.add(new Location("19", "Chung Chi Garden", "何草", "CCG", 22.417188, 114.211633, 1));
        this.Locationlist.add(new Location("20", "William M.W. Mong Engineering Building G/F", "蒙民偉工程學大樓地下", "ERB(G/F)", 22.418181, 114.207974, 2));
        this.Locationlist.add(new Location("21", "Ho Sin-Hang Engineering Building 5/F", "何善衡工程學大樓5樓", "SHB(5/F)", 22.418163, 114.207327, 2));
        this.Locationlist.add(new Location("22", "S.H. Ho College", "善衡書院", "SHHO", 22.418101, 114.210258, 1));
        this.Locationlist.add(new Location("23", "Morningside College", "晨興書院", "MC", 22.418864, 114.210307, 1));
        this.Locationlist.add(new Location("24", "Pentecostal Mission Hall Complex(Low Block)", "五旬節會樓低座", "PMHCL", 22.418805, 114.209715, 1));
        this.Locationlist.add(new Location("25", "University Health Centre", "大學保健醫療中心", "UHC", 22.419465, 114.210233, 1));
        this.Locationlist.add(new Location("26", "Mong Man Wai Building", "蒙民偉樓", "MMW", 22.419675, 114.209239, 2));
        this.Locationlist.add(new Location("27", "Choh-Ming Li Basic Medical Science Building", "李卓敏基本醫學大樓", "BMSB", 22.419601, 114.208685, 2));
        this.Locationlist.add(new Location("28", "Science Centre North Block", "科學館北座高錕樓", "SCN", 22.419784, 114.207981, 2));
        this.Locationlist.add(new Location("29", "Science Centre South Block", "科學館南座馬臨樓", "SCS", 22.419486, 114.207979, 2));
        this.Locationlist.add(new Location("30", "Science Centre East Block", "科學館東座", "SCE", 22.418831, 114.207754, 2));
        this.Locationlist.add(new Location("31", "The University Mall", "林蔭大道", "Mall", 22.419491, 114.206975, 2));
        this.Locationlist.add(new Location("32", "Lady Shaw Building", "邵逸夫夫人樓", "LSB", 22.418642, 114.206551, 2));
        this.Locationlist.add(new Location("33", "Pi Ch'iu Building", "碧秋樓", "PCB", 22.419618, 114.206381, 2));
        this.Locationlist.add(new Location("34", "Sui Loong Pao Building", "兆龍樓", "SLP", 22.419628, 114.205823, 2));
        this.Locationlist.add(new Location("35", "Y.C. Liang Hall", "潤昌堂", "LHC", 22.419901, 114.206538, 2));
        this.Locationlist.add(new Location("36", "Sir Run Run Shaw Hall", "邵逸夫堂", "SRR", 22.419908, 114.206991, 2));
        this.Locationlist.add(new Location("37", "Art Museum", "文物館", "AM", 22.419371, 114.206178, 2));
        this.Locationlist.add(new Location("38", "Institute of Chinese Studies", "中國文化研究所", "ICS", 22.419253, 114.205597, 2));
        this.Locationlist.add(new Location("39", "University Administration Building", "大學行政樓", "UAB", 22.419104, 114.205129, 2));
        this.Locationlist.add(new Location("40", "University Library", "大學圖書館", "U Lib", 22.419448, 114.205144, 2));
        this.Locationlist.add(new Location("41", "Lee Shau Kee Building", "李兆基樓", "LSK", 22.419681, 114.204076, 2));
        this.Locationlist.add(new Location("42", "Li Dak Sum Building", "李達三樓", "LDS", 22.419364, 114.204068, 2));
        this.Locationlist.add(new Location("43", "Fung King Hey Building", "馮景禧樓", "FKH", 22.419578, 114.203314, 2));
        this.Locationlist.add(new Location("44", "Leung Kau Kui Building", "梁銶琚樓", "KKB", 22.419998, 114.202837, 2));
        this.Locationlist.add(new Location("45", "John Fulton Centre", "富爾敦樓", "JFC", 22.418381, 114.204595, 2));
        this.Locationlist.add(new Location("46", "Park'n Shop Supermarket", "百佳超級市場", "Market", 22.418147, 114.204701, 2));
        this.Locationlist.add(new Location("47", "Swimming Pool", "游泳池", "Pool", 22.418081, 114.205306, 2));
        this.Locationlist.add(new Location("48", "Benjamin Franklin Centre", "范克廉樓", "BFC", 22.418181, 114.205202, 2));
        this.Locationlist.add(new Location("49", "Benjamin Franklin Centre Coffee Corner", "范克廉樓咖啡閣", "Coffee Con", 22.418353, 114.205563, 2));
        this.Locationlist.add(new Location("50", "Benjamin Franklin Centre Student Canteen", "范克廉樓學生膳堂", "Franklin", 22.418345, 114.205227, 2));
        this.Locationlist.add(new Location("51", "Women Workers' Cooperation", "女工合作社", "WWC", 22.418206, 114.205561, 2));
        this.Locationlist.add(new Location("52", "Cultural Square", "文化廣場", "CS", 22.418457, 114.205569, 2));
        this.Locationlist.add(new Location("53", "William M.W. Mong Engineering Building 9/F", "蒙民偉工程學大樓9樓", "ERB(9/F)", 22.418181, 114.207974, 2));
        this.Locationlist.add(new Location("54", "Staff Student Centre Leung Hung Kee Building", "樂群館梁雄姬樓", "LHKB", 22.421110, 114.209239, 3));
        this.Locationlist.add(new Location("55", "Ch'ien Mu Library", "錢穆圖書館", "NA Lib", 22.421460, 114.208331, 3));
        this.Locationlist.add(new Location("56", "Cheng Ming Building", "誠明館", "NAA", 22.421257, 114.207975, 3));
        this.Locationlist.add(new Location("57", "Humanities Building", "人文館", "NAH", 22.421628, 114.208171, 3));
        this.Locationlist.add(new Location("58", "Pavilion of Harmony", "合一亭", "POH", 22.421556, 114.209870, 3));
        this.Locationlist.add(new Location("59", "Cheung Chuk Shan Amenities Building", "張祝珊師生康樂大樓", "CCSAB", 22.421022, 114.205709, 3));
        this.Locationlist.add(new Location("60", "Wu Chung Multimedia Library", "胡忠多媒體圖書館", "UC Lib", 22.420897, 114.204617, 3));
        this.Locationlist.add(new Location("61", "Tsang Shiu Tim Building", "曾肇添樓", "UCA", 22.420548, 114.204841, 3));
        this.Locationlist.add(new Location("62", "T.C. Cheng Building", "鄭棟材樓", "UCC", 22.421150, 114.204852, 3));
        this.Locationlist.add(new Location("63", "Lee Woo Sing College", "和聲書院", "WS", 22.422352, 114.204334, 3));
        this.Locationlist.add(new Location("64", "Wu Yee Sun College", "伍宜孫書院", "WYS", 22.421983, 114.202554, 3));
        this.Locationlist.add(new Location("65", "Shaw College Lecture Theatre", "逸夫書院大講堂", "SWC", 22.422517, 114.201511, 3));
        this.Locationlist.add(new Location("66", "Wen Lan Tang", "文瀾堂", "WLS", 22.423174, 114.201739, 3));
        this.Locationlist.add(new Location("67", "Huen Wing Ming Building", "禤永明樓多功能學習中心", "HWM", 22.422738, 114.201931, 3));
        this.Locationlist.add(new Location("68", "Shaw College Gymnasium", "逸夫書院體育館", "Shaw gym", 22.422353, 114.200777, 3));
        this.Locationlist.add(new Location("69", "William M.W. Mong Engineering Building 4/F", "蒙民偉工程學大樓4樓", "ERB(4/F)", 22.418181, 114.207974, 2));
        this.Locationlist.add(new Location("70", "Ho Sin-Hang Engineering Building G/F", "何善衡工程學大樓地下", "SHB(G/F)", 22.418163, 114.207327, 2));
        this.Locationlist.add(new Location("71", "C.W.Chu College", "敬文書院","CWC", 22.425401, 114.206614, 3));
        this.Locationlist.add(new Location("72","Lo Kwee-Seong Integrated Biomedical Sciences Building","羅桂祥綜合生物醫學大樓,","LKS",22.427551, 114.204311, 3));
        this.Locationlist.add(new Location("73", "Jockey Club Postgraduate Hall", "賽馬會研究生宿舍", "JCPH", 22.420279, 114.212071, 1));
        this.Locationlist.add(new Location("b0", "Univ. MTR Station Stop", "港鐵大學站", "Univ. MTR Station", 22.414517, 114.210191, 1));
        this.Locationlist.add(new Location("b1", "Univ. Sports Centre Stop", "大學體育中心站", "Univ. Sports Centre", 22.417825, 114.210344, 1));
        this.Locationlist.add(new Location("b2", "Sir Run Run Shaw Hall Stop", "邵逸夫堂站", "Sir Run Run Shaw Hall", 22.419843, 114.206968, 2));
        this.Locationlist.add(new Location("b3", "Univ. Admin. Bldg. Stop", "大學行政樓站", "Univ. Admin. Bldg.", 22.418803, 114.205318, 2));
        this.Locationlist.add(new Location("b4", "S.H. Ho College Stop", "善衡書院站", "S.H. Ho College", 22.418044, 114.209872, 1));
        this.Locationlist.add(new Location("b5", "Jockey Club Postgraduate Hall Stop", "賽馬會研究生宿舍站", "Jockey Club Postgraduate Hall", 22.420386, 114.212144, 1));
        this.Locationlist.add(new Location("b6", "Fung King-heyBldg. Stop", "馮景禧樓站", "Fung King-heyBldg.", 22.419889, 114.203005, 2));
        this.Locationlist.add(new Location("b7", "United College (Upper) Stop", "聯合書院(上)站", "United College (Upper)", 22.420395, 114.205327, 3));
        this.Locationlist.add(new Location("b8", "New Asia College(Lower) Stop", "新亞書院(下)站", "New Asia College(Lower)", 22.421298, 114.207494, 3));
        this.Locationlist.add(new Location("b9", "United College (Lower) Stop", "聯合書院(下)站", "United College (Lower)", 22.420346, 114.205321, 3));
        this.Locationlist.add(new Location("b10", "Y.I.A.P. Stop", "康本園站", "Y.I.A.P.", 22.416004, 114.210832, 1));
        this.Locationlist.add(new Location("b11", "Science Centre Stop", "科學館站", "Science Centre", 22.419845, 114.207239, 2));
        this.Locationlist.add(new Location("b12", "Residences 3 & 4(Upper) Stop", "三、四苑(上)站", "Residences 3 & 4(Upper)", 22.421389, 114.203481, 3));
        this.Locationlist.add(new Location("b13", "Shaw College Stop", "逸夫書院站", "Shaw College", 22.422445, 114.201238, 3));
        this.Locationlist.add(new Location("b14", "CWC(Back) Stop", "敬文書院(後)站", "CWC(Back)", 22.425665, 114.206069, 3));
        this.Locationlist.add(new Location("b15", "Residence 15 Stop", "十五苑站", "Residence 15", 22.423781, 114.206614, 3));
        this.Locationlist.add(new Location("b16", "United College Staff Residence Stop", "聯合苑站", "United College Staff Residence", 22.423258, 114.205129, 3));
        this.Locationlist.add(new Location("b17", "Chan Chun Ha Hostel Stop", "陳震夏宿舍站", "Chan Chun Ha Hostel", 22.421872, 114.204637, 3));
        this.Locationlist.add(new Location("b18", "Residences 3 & 4 (Lower) Stop", "三、四苑 (下)站", "Residences 3 & 4 (Lower)", 22.421239, 114.203499, 3));
        this.Locationlist.add(new Location("b19", "CWC(Front) Stop", "敬文書院(前)站", "CWC(Front)", 22.425579, 114.206249, 3));
        this.Locationlist.add(new Location("b20", "Area 39 Stop", "39 區站", "Area 39", 22.427561, 114.204391, 3));
        this.Locationlist.add(new Location("b21", "Chung Chi Teaching Blocks Stop", "崇基教學樓站", "Chung Chi Teaching Blocks", 22.415901, 114.208291, 1));
        this.Locationlist.add(new Location("b22", "New Asia College(Upper) Stop", "新亞書院(上)站", "New Asia College(Upper)", 22.421124, 114.207798, 3));
        this.Locationlist.add(new Location("b23", "Residence 10 Stop", "十苑站", "Residence 10", 22.424579, 114.208036, 3));
        this.Locationlist.add(new Location("b24", "Piazza Stop", "港鐵大學站廣場站", "Piazza", 22.413806, 114.209441, 1));
    }

    public void addLocationPhoto(){
        /*Photo URL modification is needed (current: use id as ref. todo: change to firebase URL)*/
        this.Locationlist.add(new Location("0", "Cheng Yu Tung Building", "鄭裕彤樓", "CYT", 22.412462, 114.210433, 1, "p0_1_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 408","工商管理學院","Business School Undergraduate Office","3943 7746"));
                    add(new Centre("13/F","劉佐德全球經濟及金融研究所","Lau Chor Tak Institute of Global Economics and Finance","3943 1620"));
                    add(new Centre("Room 1201","經濟及金融研究中心","Center for Economics and Finance","3943 1777"));
                    add(new Centre("Room 1201","金融學系","Department of Finance","3943 7805"));
                    add(new Centre("Room 1154","何鴻燊海量數據決策分析研究中心","Stanley Ho Big Data Decision Analytics Research Centre ","3943 9590"));
                    add(new Centre("Room 1133","市場工程中心","Center for Marketing Engineering","3943 1639"));
                    add(new Centre("Room 1101","市場學系","Department of Marketing","3943 7809"));
                    add(new Centre("10/F","公司治理中心","Center for Institutions and Governance","3943 8691"));
                    add(new Centre("Room 1053","會計學院","School of Accountancy","3943 7255"));
                    add(new Centre("Room 951","創業研究中心","Center for Entrepreneurship","3943 7542 "));
                    add(new Centre("Room 949","亞洲供應鏈及物流研究所","Asian Institute of Supply Chains & Logistics","3943 4493"));
                    add(new Centre("Room 901","決策科學與企業經濟學系","Department of Decision Sciences and Managerial Economics","3943 7813"));
                    add(new Centre("Room 845","國際商業研究中心","Center for International Business Studies","N/A"));
                    add(new Centre("Room 846","家族企業研究中心","Center for Family Business","3943 7180"));
                    add(new Centre("Room 803a","管理學系","Department of Management","3943 7898"));
                    add(new Centre("Room 743","中國金融發展與改革研究中心","Center for Chinese Financial Development and Reform","3943 7896"));
                    add(new Centre("Room 715","酒店、旅遊及不動產研究中心","Center for Hospitality and Real Estate Research","3943 8798"));
                    add(new Centre("Room 701","酒店及旅遊管理學院","School of Hotel and Tourism Management","3943 8593"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
        }}
        ));

        this.Locationlist.add(new Location("1", "MTR Station", "港鐵站", "MTR", 22.413766, 114.209802, 1, "p1_0_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("2", "Esther Lee Building", "利黃瑤璧樓", "ELB", 22.414255, 114.208451, 1, "p2_1_00",
                new ArrayList<Centre>(){{
                    add(new Centre("6/F","那打素護理學院","The Nethersole School of Nursing","3943 8174"));
                    add(new Centre("9/F, 10/F","經濟系","Department of Economics ","3943 4368"));
                    add(new Centre("Room 507","香港亞太研究所","Hong Kong Institute of Asia-Pacific Studies","3943 6740"));
                    add(new Centre("Room 507","經濟研究中心","Economic Research Centre","3943 6762"));
                    add(new Centre("Room 507","華人家庭研究中心","Centre for Chinese Family Studies","3943 1209"));
                    add(new Centre("Room 507D","全球中國研究計劃","Global China Research Programme","3943 6738"));
                    add(new Centre("Room 507","性別研究中心","Gender Research Centre","3943 8775"));
                    add(new Centre("Room 507","中國城市住宅硏究中心","Centre for Housing Innovations","2994 0495"));
                    add(new Centre("Room 507","國際事務研究中心","International Affairs Research Centre","N/A"));
                    add(new Centre("Room 507","公共政策研究中心","Public Policy Research Centre","3943 6761 "));
                    add(new Centre("Room 507","生活質素研究中心","Hong Kong Institute of Asia-Pacific Studies","3943 3400"));
                    add(new Centre("Room 507","社會與政治發展研究中心","Centre for Social and Political Development Studies","3943 1323"));
                    add(new Centre("Room 507","城市與區域發展研究中心","Research Centre for Urban and Regional Development","3943 6746"));
                    add(new Centre("Room 507","青年研究中心","Centre for Youth Studies","3943 3473"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("1/F"));
        }}
        ));

        this.Locationlist.add(new Location("3", "Yasumoto International Academic Park", "康本國際學術園", "YIA", 22.416051, 114.210907, 1, "p3_1_00",
                new ArrayList<Centre>(){{
                    add(new Centre("13/F 1303","學術交流處","Office of Academic Links","3943 7597"));
                    add(new Centre("13/F 1306","學術交流處（國內事務）","Office of Academic Links (China)","3943 8727"));
                    add(new Centre("12/F 1202","入學及學生資助處","Office of Admissions and Financial Aid","3943 8951"));
                    add(new Centre("Room 1102","中國研究中心","Center for China Studies","N/A"));
                    add(new Centre("10/F 11/F","註冊及考試組","Registration and Examination Section, Registry","3943 9888"));
                    add(new Centre("Room 903","環境、能源及可持續發展研究所","Institute of Environment, Energy and Sustainability, CUHK","3943 5272"));
                    add(new Centre("Suite 602","香港中文大學賽馬會老年學研究","CUHK Jockey Club Institute of Ageing","3943 9450"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("2/F"));
            add(new WaterCooler("4/F"));
            add(new WaterCooler("5/F"));
            add(new WaterCooler("6/F"));
        }}
        ));

        this.Locationlist.add(new Location("4", "Wu Ho Man Yuen Building", "伍何曼原樓", "WMY", 22.416891, 114.211708, 1, "p4_3_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 804","中大通發証中心","CU Link Card Centre","3943 8507"));
                    add(new Centre("802, 803","資訊科技培訓中心","Student IT Proficiency Test","3943 3900"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("2/F"));
            add(new WaterCooler("3/F"));
            add(new WaterCooler("4/F"));
            add(new WaterCooler("6/F"));
            add(new WaterCooler("8/F"));
        }}
        ));

        this.Locationlist.add(new Location("5", "An Integrated Teaching Building", "綜合教學大樓", "ATI", 22.416149, 114.211393, 1, "p5_3_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 106","建築系","School of Architecture","3943 6583"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("2/F"));
            add(new WaterCooler("3/F"));
            add(new WaterCooler("4/F"));
        }}
        ));
        this.Locationlist.add(new Location("6", "Ho Tim Building", "何添樓", "HTB", 22.415261, 114.207715, 1, "P6_2_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Not Specific","教育學院","Faculty of Education","3943 6937"));
                    add(new Centre("Room 210","課程與教學學系","Department of Curriculum and Instruction","3943 8197"));
                    add(new Centre("Room 209","教育行政與政策學系","Department of Educational Administration and Policy","3943 6953"));
                    add(new Centre("Room 208","教育心理學學系","Department of Educational Psychology","3943 6904"));
                    add(new Centre(" Room 204","香港教育研究所","Hong Kong Institute of Educational Research","3943 6999"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("7", "Wong Foo Yuan Building", "王福元樓", "FYB", 22.415138, 114.207516, 1, "p7_6_00",
                new ArrayList<Centre>(){{
                    add(new Centre("2/F","地理與資源管理學系","Department of Geography and Resource Management","3943 6532"));
                    add(new Centre("Room 406b","未來城市研究所","Institute Of Future Cities","3943 5263"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("8", "Chen Kou Bun Building", "陳國本樓", "CKB", 22.415299, 114.207404, 1, "p8_7_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 609","普通話教育研究及發展中心","Centre for Research and Development of Putonghua Education","3943 6749"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));
        this.Locationlist.add(new Location("9", "Sino Building", "信和樓", "SB", 22.415461, 114.207311, 1, "p9_7_00",
                new ArrayList<Centre>(){{
                    add(new Centre("3/F","心理學系","Department of Psychology","3943 6650"));
                    add(new Centre("Room 612","香港學生能力國際評估中心","Hong Kong Centre for International Student Assessment","2603 7209"));
                    add(new Centre("Room 111","社會科學院","Faculty of Social Science ","3943 1495"));
                    add(new Centre("Room 431","社會系","Department of Sociology","3943 6604"));
                    add(new Centre("Room 204","香港教育領導發展中心","Hong Kong Centre for the Development of Educational Leadership","3943 6929"));
                    add(new Centre("NA","學習科學與科技中心","Centre for Learning Sciences and Technologies","2603 6729"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("4/F"));
        }}
        ));

        this.Locationlist.add(new Location("10", "Hui Yeung Shing Building", "許讓成樓", "HYS", 22.414692, 114.207872, 1, "p10_2_00",
                new ArrayList<Centre>(){{
                    add(new Centre("2/F","音樂系","Department of Music","3943 6716"));
                    add(new Centre("4/F","大腦與認知研究所","Brain and Mind Institute","N/A"));
                    add(new Centre("Room 502","學能提升研究中心","Centre for Learning Enhancement And Research ","3943 6201"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("11", "Orchid Lodge", "蘭苑", "OL Can", 22.415526, 114.207678, 1, "p11_6_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("12", "Chung Chi College Staff Club Clubhouse", "聯誼會", "CCCS", 22.415986, 114.207531, 1, "P12_11_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("13", "Li Wai Chun Building", "李慧珍樓", "LWCB", 22.415011, 114.207136, 1, "p13_7_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room G07","中醫學院","School of Chinese Medicine","3943 4328"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("14", "Chung Chi Tang", "眾志堂", "CC Can", 22.416511, 114.209789, 1, "p14_3_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("崇基學院學生膳堂", "Chung Chi College Student Canteen"));
        }}
        ));

        this.Locationlist.add(new Location("15", "Chung Chi College Chapel", "崇基教堂", "CCCC", 22.415952, 114.207189, 1, "p15_12_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("16", "Elisabeth Luce Moore Library", "牟路思怡圖書館", "CC Lib", 22.416375, 114.208772, 1, "p16_11_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("17", "Lake Ad Excellentiam", "未圓湖", "Lake", 22.416031, 114.209321, 1, "p17_14_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("18", "Pommerenke Student Centre", "龐萬倫學生中心禮堂", "PSC", 22.417105, 114.208838, 1, "p18_14_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("3/F"));
        }}
        ));

        this.Locationlist.add(new Location("19", "Chung Chi Garden", "何草", "CCG", 22.417188, 114.211633, 1, "p19_4_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("20", "William M.W. Mong Engineering Building G/F", "蒙民偉工程學大樓地下", "ERB(G/F)", 22.418181, 114.207974, 2, "p20_18_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 609","系統工程與工程管理學系","Department of Systems Engineering and Engineering Management","3943 8313"));
                    add(new Centre("Room 702","信興高等工程研究所","Shun Hing Institute of Advanced Engineering","3943 4351 "));
                    add(new Centre("Room 702","生物醫學工程研究中心","Biomedical and Bioinformatics Research Centre","3943 4351 "));
                    add(new Centre("Room 702","多媒體工程研究中心","Multimedia Research Centre","3943 4351 "));
                    add(new Centre("Room 702","可再生能源工程研究範疇","Renewable Energy Track","3943 4351 "));
                    add(new Centre("Room 209","精密工程研究","Institute of Precision Engineering","N/A"));
                    add(new Centre("Room 213","機械與自動化工程學系","Department of Mechanical and Automation Engineering","3943 8337"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("21", "Ho Sin-Hang Engineering Building 5/F", "何善衡工程學大樓5樓", "SHB(5/F)", 22.418163, 114.207327, 2, "p21_29_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 606","工程學院","Faculty of Engineering","3943 1109"));
                    add(new Centre("Room 607","理論計算機科學與通訊科學研究所","Institute of Theoretical Computer Science and Communications","3943 1257"));
                    add(new Centre("Room 1028","計算機科學與工程學系","Department of Computer Science and Engineering","3943 8444"));
                    add(new Centre("Room 834","信息工程學系","Department of Information Engineering","3943 8385"));
                    add(new Centre("Room 802","移動技術中心","Mobitec Technologies Centre","3943 8206"));
                    add(new Centre("Room 727 - 738","網絡編碼研究所","Institute of Network Coding","3943 8388"));
                    add(new Centre("Room 601","創新與技術中心","Centre for Innovation and Technology","3943 8221"));
                    add(new Centre("Room 404","電子工程學系","Department of Electronic Engineering","3943 8254"));
                    add(new Centre("Room 404","光電子研究中心","Centre for Advanced Research in Photonics","N/A"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("22", "S.H. Ho College", "善衡書院", "SHHO", 22.418101, 114.210258, 1, "p22_19_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("1/F", "善衡書院餐廳", "Canteen of S.H. Ho College"));
        }}
        ));

        this.Locationlist.add(new Location("23", "Morningside College", "晨興書院", "MC", 22.418864, 114.210307, 1, "p23_22_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("24", "Pentecostal Mission Hall Complex(Low Block)", "五旬節會樓低座", "PMHCL", 22.418805, 114.209715, 1, "p24_22_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("25", "University Health Centre", "大學保健醫療中心", "UHC", 22.419465, 114.210233, 1, "p25_24_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Inside","大學保健處","University Health Service","3943 6422"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("26", "Mong Man Wai Building", "蒙民偉樓", "MMW", 22.419675, 114.209239, 2, "p26_24_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("27", "Choh-Ming Li Basic Medical Science Building", "李卓敏基本醫學大樓", "BMSB", 22.419601, 114.208685, 2, "p27_26_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 612","生物醫學學院","School of Biomedical Sciences","3943 6818"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F", "李卓敏基本醫學大樓小食店", "Basic Medical Sciences Building Snack Bar"));
        }}
        ));

        this.Locationlist.add(new Location("28", "Science Centre North Block", "科學館北座高錕樓", "SCN", 22.419784, 114.207981, 2, "p28_29_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room G43","理學院","Science of Faculty","3943 6327"));
                    add(new Centre("Room 108","物理系","Department of Physics","3943 6154"));
                    add(new Centre("Room 108","光學研究中心","Centre for Optical Sciences","N/A"));
                    add(new Centre("Room 108","理論物理研究所","Institute of Theoretical Physics","3943 6339"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("29", "Science Centre South Block", "科學館南座馬臨樓", "SCS", 22.419486, 114.207979, 2, "p29_21_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room G59","化學系","Department of Chemistry","3943 6344"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("30", "Science Centre East Block", "科學館東座", "SCE", 22.418831, 114.207754, 2, "p30_21_00",
                new ArrayList<Centre>(){{
                    add(new Centre("2/F & 3/F","中醫中藥研究所","Institute of Chinese Medicine","3943 4370"));
                    add(new Centre("Room EG02","植物分子生物學及農業生物科技研究所","UGC-AoE Centre for Plant & Agricultural Biotechnology","3943 5593"));
                    add(new Centre("Room EG02","教資會卓越學科領域植物及農業生物科技中心","Institute of Plant Molecular Biology and Agricultural Biotechnology","3943 8133"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("LG/F"));
        }}
        ));

        this.Locationlist.add(new Location("31", "The University Mall", "林蔭大道", "Mall", 22.419491, 114.206975, 2, "p31_21_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("32", "Lady Shaw Building", "邵逸夫夫人樓", "LSB", 22.418642, 114.206551, 2, "p32_21_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 220","數學系","Department of Mathematics","3943 7988"));
                    add(new Centre("Room 119","統計學系","Department of Statistics","3943 7931"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("33", "Pi Ch'iu Building", "碧秋樓", "PCB", 22.419618, 114.206381, 2, "p33_31_00",
                new ArrayList<Centre>(){{
                    add(new Centre("G/F 2/F","資訊科技服務處辦公室","ITSC General Office","3943 8801"));
                    add(new Centre("Room 301","研究及知識轉移服務處","Office of Research and Knowledge Transfer Services","3943 9881"));
                    add(new Centre("Room 109A","資訊科技服務處服務台","ITSC Service Desk","3943 8845"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("34", "Sui Loong Pao Building", "兆龍樓", "SLP", 22.419628, 114.205823, 2, "p34_37_00",
                new ArrayList<Centre>(){{
                }},new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("35", "Y.C. Liang Hall", "潤昌堂", "LHC", 22.419901, 114.206538, 2, "p35_33_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("1/F"));
        }}
        ));

        this.Locationlist.add(new Location("36", "Sir Run Run Shaw Hall", "邵逸夫堂", "SRR", 22.419908, 114.206991, 2, "p36_31_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Inside","藝術行政主任辦公室","The Office of the Arts Administrator","3943 7852"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));
        this.Locationlist.add(new Location("37", "Art Museum", "文物館", "AM", 22.419371, 114.206178, 2, "p37_33_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Inside","文物館","Art Museum","3943 7416"));
                    add(new Centre("East/東翼二樓","中國考古藝術研究中心","Center For Chinese Archaeology And Art","3943 7371"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("38", "Institute of Chinese Studies", "中國文化研究所", "ICS", 22.419253, 114.205597, 2, "p38_37_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 105","中國文化研究所","Institute of Chinese Studies","3943 7394"));
                    add(new Centre("Room G23","吳多泰中國語文研究中心","T.T. Ng Chinese Language Research Centre","3943 7392"));
                    add(new Centre("Room G06 ","當代中國文化研究中心","Research Centre for Contemporary Chinese Culture","3943 7382"));
                    add(new Centre("Inside","劉殿爵中國古籍研究中心","D.C. Lau Research Centre for Chinese Ancient Texts","3943 7381"));
                    add(new Centre("Inside","翻譯研究中心","Research Centre for Translation ","3943 7399 "));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("39", "University Administration Building", "大學行政樓", "UAB", 22.419104, 114.205129, 2, "p39_32_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 217","深圳研究院","Shenzhen Research Institute","3943 1683"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("40", "University Library", "大學圖書館", "U Lib", 22.419448, 114.205144, 2, "p40_34_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("LG/F"));
            add(new WaterCooler("G/F", "學習共享空間", "Learning Common"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("2/F"));
            add(new WaterCooler("3/F"));
            add(new WaterCooler("4/F"));
        }}
        ));

        this.Locationlist.add(new Location("41", "Lee Shau Kee Building", "李兆基樓", "LSK", 22.419681, 114.204076, 2, "p41_42_00",
                new ArrayList<Centre>(){{
                    add(new Centre("6/F","法律學院","Faculty of Law","3943 4310"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("42", "Li Dak Sum Building", "李達三樓", "LDS", 22.419364, 114.204068, 2, "p42_41_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 436","英語教學單位","English Language Teaching Unit","3943 7465"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("43", "Fung King Hey Building", "馮景禧樓", "FKH", 22.419578, 114.203314, 2, "p43_41_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 523","中國語言及文學系","Department of Chinese Language and Literature","3943 7074"));
                    add(new Centre("Room 334","英文系","Department of English",""));
                    add(new Centre("3/F","人文價值研究中心","Research Centre for Human Values","3943 7001"));
                    add(new Centre("4/F","哲學系","Department of Philosophy","3943 7135"));
                    add(new Centre("Room 131","歷史系","Department of History","3943 7117"));
                    add(new Centre("Room G26B","鄭承隆基金亞洲現象學中心","Edwin Cheng Foundation Asian Centre for Phenomenology","3943 8524"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("馮景禧樓", "Swire Hall"));
        }}
        ));

        this.Locationlist.add(new Location("44", "Leung Kau Kui Building", "梁銶琚樓", "KKB", 22.419998, 114.202837, 2, "p44_41_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 301","文化及宗教研究系","Department of Cultural and Religious Studies ","3943 1269"));
                    add(new Centre("Room 402","梁保全香港歷史及人文研究中心","Leung Po Chuen Research Centre for Hong Kong History and Humanities","3943 5796"));
                    add(new Centre("4/F","日本研究學系","Department of Japanese Studies","3943 6563"));
                    add(new Centre("1/F","翻譯系","Department of Translation","3943 7700"));
                    add(new Centre("G/F","語言學及現代語言系","Department of Linguistics and Modern Languages","3943 7911"));
                    add(new Centre("LG 13C","臺灣研究中心","Taiwan Research Centre","3943 1694"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("45", "John Fulton Centre", "富爾敦樓", "JFC", 22.418381, 114.204595, 2, "p45_42_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 212/214","學生設施組","Student Amenities Section","3943 3733"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("46", "Park'n Shop Supermarket", "百佳超級市場", "Market", 22.418147, 114.204701, 2, "p46_45_00",
                new ArrayList<Centre>(){{
                    add(new Centre("LG, John Fulton Center","百佳超級市場","Park'n Shop Supermarket","2603 5432"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("47", "Swimming Pool", "游泳池", "Pool", 22.418081, 114.205306, 2, "p47_51_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("48", "Benjamin Franklin Centre", "范克廉樓", "BFC", 22.418181, 114.205202, 2, "p48_50_00",
                new ArrayList<Centre>(){{
                    add(new Centre("2/F","大學輔導長辦公室","Office of the University Dean of Students","N/A"));
                    add(new Centre("2/F","學生事務處處長辦公室","Director's Office","N/A"));
                    add(new Centre("2/F","就業策劃及發展中心","Career Planning and Development Centre","3943 7202"));
                    add(new Centre("1/F","學生輔導及發展組","Student Counselling and Development Service","3943 7208"));
                    add(new Centre("1/F","來港生組","Incoming Students Section","3943 7945"));
                    add(new Centre("1/F","學生活動組","Student Activities Section","3943 7323"));
                    add(new Centre("1/F","學生服務中心","Student Services Centre","3943 8652"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("49", "Benjamin Franklin Centre Coffee Corner", "范克廉樓咖啡閣", "Coffee Con", 22.418353, 114.205563, 2, "p49_50_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F", "范克廉樓咖啡閣", "Benjamin Franklin Centre Coffee Corner"));
        }}
        ));

        this.Locationlist.add(new Location("50", "Benjamin Franklin Centre Student Canteen", "范克廉樓學生膳堂", "Franklin", 22.418345, 114.205227, 2, "p50_45_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F", "范克廉樓學生膳堂", "Benjamin Franklin Centre Student Canteen"));
        }}
        ));

        this.Locationlist.add(new Location("51", "Women Workers' Cooperation", "女工合作社", "WWC", 22.418206, 114.205561, 2, "p51_47_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("52", "Cultural Square", "文化廣場", "CS", 22.418457, 114.205569, 2, "p52_15_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("53", "William M.W. Mong Engineering Building 9/F", "蒙民偉工程學大樓9樓", "ERB(9/F)", 22.418181, 114.207974, 2, "p53_20_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("54", "Staff Student Centre Leung Hung Kee Building", "樂群館梁雄姬樓", "LHKB", 22.421110, 114.209239, 3, "p54_55_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F", "新亞書院學生餐廳", "New Asia College Student Canteen"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("55", "Ch'ien Mu Library", "錢穆圖書館", "NA Lib", 22.421460, 114.208331, 3, "p55_54_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("1/F"));
        }}
        ));

        this.Locationlist.add(new Location("56", "Cheng Ming Building", "誠明館", "NAA", 22.421257, 114.207975, 3, "p56_54_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 219","藝術系","Department of Fine Arts","3943 7615"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("57", "Humanities Building", "人文館", "NAH", 22.421628, 114.208171, 3, "p57_55_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 407","人類學系","Department of Anthropology","3943 7670"));
                    add(new Centre("Room 206-207","新聞與傳播學院","School of Journalism and Communication","3943 7680"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("58", "Pavilion of Harmony", "合一亭", "POH", 22.421556, 114.209870, 3, "p58_26_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("59", "Cheung Chuk Shan Amenities Building", "張祝珊師生康樂大樓", "CCSAB", 22.421022, 114.205709, 3, "p59_56_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F", "聯合書院學生膳堂", "United College Student Canteen"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("60", "Wu Chung Multimedia Library", "胡忠多媒體圖書館", "UC Lib", 22.420897, 114.204617, 3, "p60_61_00",
                new ArrayList<Centre>(){{
                    add(new Centre("1/F","自學中心","Independent Learning Centre","3943 8733"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("1/F"));
            add(new WaterCooler("2/F"));
        }}
        ));

        this.Locationlist.add(new Location("61", "Tsang Shiu Tim Building", "曾肇添樓", "UCA", 22.420548, 114.204841, 3, "p61_59_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Room 308","香港中文大學─北京大學─台灣聯合大學系統語言與人類複雜系統聯合研究中心","Joint Research Centre for Language and Human Complexity","3943 5529"));
                    add(new Centre("Room 308","人文學科研究所","Research Institute for the Humanities","3943 8698"));
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("62", "T.C. Cheng Building", "鄭棟材樓", "UCC", 22.421150, 114.204852, 3, "p62_60_00",
                new ArrayList<Centre>(){{
                    add(new Centre("Level 4&5","社會工作學系","Department of Social Work","3943 7507"));
                    add(new Centre("3/F","政治與行政學系","Department of Government and Public Administration","3943 7530"));
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("G/F"));
            add(new WaterCooler("5/F"));
        }}
        ));

        this.Locationlist.add(new Location("63", "Lee Woo Sing College", "和聲書院", "WS", 22.422352, 114.204334, 3, "p63_41_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("64", "Wu Yee Sun College", "伍宜孫書院", "WYS", 22.421983, 114.202554, 3, "p64_41_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("65", "Shaw College Lecture Theatre", "逸夫書院大講堂", "SWC", 22.422517, 114.201511, 3, "p65_66_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("66", "Wen Lan Tang", "文瀾堂", "WLS", 22.423174, 114.201739, 3, "p66_65_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("LG4/F"));
            add(new WaterCooler("LG3/F"));
        }}
        ));

        this.Locationlist.add(new Location("67", "Huen Wing Ming Building", "禤永明樓多功能學習中心", "HWM", 22.422738, 114.201931, 3, "p67_65_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("68", "Shaw College Gymnasium", "逸夫書院體育館", "Shaw gym", 22.422353, 114.200777, 3, "p68_65_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
        }}
        ));

        this.Locationlist.add(new Location("69", "William M.W. Mong Engineering Building 4/F", "蒙民偉工程學大樓4樓", "ERB(4/F)", 22.418181, 114.207974, 2, "p69_22_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("4/F"));
        }}
        ));

        this.Locationlist.add(new Location("70", "Ho Sin-Hang Engineering Building G/F", "何善衡工程學大樓地下", "SHB(G/F)", 22.418163, 114.207327, 2, "p70_69_00",
                new ArrayList<Centre>(){{
                }}, new ArrayList<WaterCooler>(){{
            add(new WaterCooler("1/F"));
            add(new WaterCooler("9/F"));
            add(new WaterCooler("10/F"));
        }}
        ));
    }

    public Location getLocation(int index){
        return Locationlist.get(index);
    }

    public List<Location> getLocationlist(){
        return Locationlist;
    }

    public int getLength(){
        return Locationlist.size();
    }

    public double[] getCoor(int index){
        return Locationlist.get(index).getCoor();
    }

}
