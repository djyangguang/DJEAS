public class BoxYieldRptControllerBean extends AbstractBoxYieldRptControllerBean
{
    private static Logger logger =
        Logger.getLogger("com.kingdee.eas.iscm.inv.app.BoxYieldRptControllerBean");

   
    protected void _SteDate(Context ctx) throws BOSException, EASBizException {
	//setBoxYieldDate(ctx);//产量事务
	//setLossDate(ctx);//毛利
	Connection conn = null;
	//PreparedStatement stmt = null;
	StringBuffer sql=new StringBuffer("");
	StringBuffer task = new StringBuffer();
	ResultSet rs=null;
	IRowSet rs1=null;
	    try{
		// 外网通时使用此代码start
		 conn = DBHelper.getConnection("192.168.2.104", "dongjing", "sa", "djdb"); // 此方法需要外网
		// 外网通时使用此代码end
	    }		    	
	    catch (Exception e)
	    {
		// 外网不通时使用此代码start
		 try {
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		 String connectionUrl =
		 "jdbc:sqlserver://192.168.2.104;database=dongjing;user=sa;password=djdb";
		 try {
		    conn = DriverManager.getConnection(connectionUrl);
		} catch (SQLException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		// 外网不通时使用此代码end
	    }	
	Calendar start = Calendar.getInstance(); 
	
        start.set(2013, 12, 01); 
        Calendar end = Calendar.getInstance(); 
        end.set(2015, 1, 30); 
         
        int sumSunday = 0; 
        int sumSat = 0; 
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        while(start.compareTo(end) <= 0) { 
            int w = start.get(Calendar.DAY_OF_WEEK); 
            if(w == Calendar.SUNDAY) 
                sumSunday ++; 
            if(w == Calendar.SATURDAY) 
                sumSunday ++; 
            //打印每天 
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.SECOND,0);
            start.set(Calendar.MINUTE,0);
            String statr1 =format.format(start.getTime());
           // System.out.println(statr1); 
            
            //循环，每次天数加1 
            start.set(Calendar.DATE, start.get(Calendar.DATE) + 1); 
            String end1=format.format(start.getTime());
           // System.out.println("+"+end1); 
           
        //    setLossDate(ctx,statr1,end1);
            deleteplanexcel(ctx,statr1,end1,conn);
        } 
       
       // 2016-04-01 00:00:00
     //+2016-05-01 00:00:00
       
  



    }
    private void deleteplanexcel(Context ctx, String statr1, String end1,
	    Connection conn) {
	System.out.println(statr1); 
	// TODO Auto-generated method stub
	
	// select * from T_PlanExecute where  Farrivedate  = '2014-01-01'
	// select Farrivedate from T_PlanExecute where  Farrivedate  > '2014-01-01' and Farrivedate<= '2014-01-02' 
	    try {
		Statement stmt = conn.createStatement();
		String sql = "delete from [dongjing].[dbo].[t_planexecute] where Farrivedate >'"+statr1+"' and Farrivedate <='"+end1+"'";
		System.out.println(sql); 
		stmt.executeUpdate(sql);
		conn.commit();	   
		    stmt.close();//关闭连接
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    Object billInfo;	   		   
	    
	
    }
   
    protected void _checkNumberDup(Context ctx, IObjectValue model) throws BOSException, EASBizException {
	     // 不需要判断number重复
	    }


    private void setLossDate(Context ctx, String format, String format2)  {
	// TODO Auto-generated method stub
	IBoxYieldRpt iDest;
	
	CoreBaseCollection baCustColl = new CoreBaseCollection();
	
	
	
	
	StringBuffer sql = new StringBuffer();
	BigDecimal auamount = new BigDecimal("0");	
	List orderlist = new ArrayList(); 
	List lossid = new ArrayList(); 
	//StringBuffer sql = new StringBuffer();
	 sql.append(" /*dialect*/ select ss.fbrowsegroupid,ss.fbizdate,nvl(ss.VORDERENTRYID,'') VORDERENTRYID,ss.customer,nvl(ss.Aamount,0)Aamount,nvl(ss.fprice,0)fprice,nvl(ss.TranslateQty,0)TranslateQty,nvl(ss.Alarea,0)Alarea,nvl(ss.xlarea,0)xlarea,nvl(ss.ordertype,0)ordertype,nvl(ss.person,'')person,ss.ids,nvl(ss.amount,0)amount,nvl(ss.entrysum,0)entrysum,nvl(ss.area,0)area,nvl(sum(ss.cc),0) Costs,ss.fseq,ss.salenumber,nvl(ss.Afid,'')Afid,ss.productid from   \r\n");
	    sql.append(" (select  t7.fbrowsegroupid,t1.fbizdate,nvl(t.FVORDERENTRYID,ae.fvorderentryid) VORDERENTRYID,t7.fname_l2 customer,t11.FUnitPrice fprice,t.FTranslateQty TranslateQty,t1.fordertype ordertype,t.fseq fseq,ae.fid Afid,t18.fname_l2 person,t7.fname_l2,t1.fnumber salenumber,t.fid ids,t.famount amount,   \r\n");
	    sql.append("t11.FUnitPrice*(t.famount-nvl(t.FTranslateQty,0))  entrysum , \r\n");
	    sql.append("      t14.fmateriallength*t14.fmaterialwidth/10000 xlarea,ta14.fmateriallength*ta14.fmaterialwidth/10000 Alarea,ae.famount Aamount,   \r\n");
	    sql.append("     ----t5.fmateriallength,t5.fmaterialwidth,t5.fbedivisor,t.famount,t5.fdivisor,     \r\n");
	    sql.append("     t.famount*(case when t14.farea is null or t14.feffected = 0 then t2.farea else t14.farea end ) area,  \r\n");
	    sql.append("     p.fnumber,  (case when pcp.FPrice is null  then p.funitcost else pcp.FPrice end )*ml.Frate cc,t2.fid productid                   \r\n");
	    sql.append("    from t_ord_saleorderentry t left join    \r\n");
	    sql.append("    t_ord_saleorder t1 on t.fparentid=t1.fid   \r\n");
	    sql.append("    left join t_pdt_productdef t2 on t.fproductid=t2.fid   \r\n");
	    sql.append("    left join t_bal_gathering t6 on t6.forderentryid=t.fid and t6.fbiztype=41   \r\n");
	    sql.append("     left join t_bd_customer t7 on t7.fid = t1.fcustomerid  left join  T_PDT_ProductProperty t8 on t8.forderentryid = t.fid   \r\n");
	    sql.append("      left join t_bal_balanceprice t9 on t9.fproductid = t2.fid    \r\n");
	    sql.append("     left join (select fparentid,min(FPrice) FPrice from T_BAL_BalancePricePrices group by fparentid) t10 on t9.fid = t10.fparentid    \r\n");
	    sql.append("     left join t_bal_gathering t11 on t11.forderentryid=t.fid  and t11.fbiztype=41   \r\n");
	    sql.append("     left join t_bd_customersaleinfo t16 on t16.fcustomerid = t7.fid   \r\n");
	    sql.append("    left join t_bd_customersaler t17 on t17.fcustomersaleid = t16.fid   \r\n");
	    sql.append("    left join t_bd_person t18 on t18.fid = t17.fpersonid  \r\n");
	    sql.append("     left join t_pdt_productproperty t14 on t14.forderentryid = t.fid  \r\n");
	    sql.append("    -- left join T_PPL_SingleCutPlan s on s.fsaleorderentryid =t.fid  \r\n");
	    sql.append("       left join T_PDT_MaterialCode m on m.fid = t2.FMaterialCodeID                                                \r\n");
	    sql.append("     left join T_PDT_MaterialCodePaperLayers ml on ml.fparentid = m.fid   \r\n");
	    sql.append("    left join T_PDT_PaperCode p on p.fid = ml.fpapercodeid  --   \r\n");
	    sql.append("    left join (select fparentid,min(Cfqprice) FPrice from CT_PDT_PaperCodePrice group by fparentid) pcp on p.fid = pcp.fparentid  --原纸代码单位成本   \r\n");
	    sql.append("     left join t_ord_saleorderentry ae on t.faorderentryid = ae.fid   \r\n");
	    sql.append("     left join t_pdt_productproperty ta14 on ta14.forderentryid = ae.fid  \r\n");
	    sql.append("      --inner join T_PRO_cardboardtask task on task.fsaleorderid=t.fid   \r\n"); // and t1.fnumber ='P1603100661'and t1.fnumber ='P1603100661'and t1.fbizdate >= to_date('2015-12-27','yyyy-mm-dd') AND t1.fbizdate < to_date('2015-12-28','yyyy-mm-dd')
	    sql.append("     where   t1.faudited = 1   and  ( t1.fnumber not like 'V%'and t1.fnumber not like 'A%' and t1.fnumber not like 'S%')    \r\n");
	    
//	    sql.append(" and  T1.Fbiztime > to_date('2015-06-15,00:00:00', 'yyyy-mm-dd hh24:mi:ss') "); //25-4月 -16 09.21.28.800000 上午
//		sql.append(" and T1.Fbiztime <=to_date('2015-07-01,00:00:00', 'yyyy-mm-dd hh24:mi:ss') "); 
	    sql.append(" and  t1.fbizdate > to_date('"+format+"', 'yyyy-mm-dd hh24:mi:ss') ");
	    sql.append(" and t1.fbizdate <=to_date('"+format2+"', 'yyyy-mm-dd hh24:mi:ss') "); 
	//    sql.append(" and t7.fid='NbksCwEVEADgAiScwKgCZ78MBA4=' and  t1.fbizdate > to_date('2016-04-10,00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
	//    sql.append(" and t1.fbizdate <=to_date('2016-04-10,00:00:00', 'yyyy-mm-dd hh24:mi:ss')"); 
	    sql.append("      order by t1.fnumber) ss    group by ss.fbrowsegroupid,ss.fbizdate,ss.VORDERENTRYID,ss.Aamount,ss.person,ss.ids,ss.amount,ss.entrysum,ss.area,ss.fseq,ss.salenumber,ss.Afid,ss.ordertype,ss.xlarea,ss.Alarea,ss.TranslateQty,ss.productid,ss.fprice,ss.customer order by ss.salenumber ,ss.fseq desc  \r\n");
	    IRowSet rs;
	    try {
		rs = SQLExecutorFactory.getLocalInstance(ctx,sql.toString()).executeSQL();
	   
	    
	    for(;rs.next();)
	    {
		//System.out.println(" saleorder count======= "+rs.size());
		//if(iDest.exists("where simplename=0 and SaleorderEntryId='"+rs.getObject("ids").toString()+"'"))
		//{
		//    lossid.add(rs.getObject("ids").toString());
		//}
		IObjectPK pk = null;
		//select ss.fbizdate,ss.VORDERENTRYID,ss.customer,ss.Aamount,ss.fprice,ss.TranslateQty,ss.Alarea,ss.xlarea,ss.ordertype,ss.person,ss.ids,ss.amount,ss.entrysum,ss.area,sum(ss.cc) Costs,ss.fseq,ss.salenumber,ss.Afid,ss.productid 
		BoxYieldRptInfo boxrptinfo = new BoxYieldRptInfo();
		boxrptinfo.setSaleorderEntryId(rs.getObject("ids").toString());
		boxrptinfo.setPdtIndetailBizTime((Date) rs.getObject("fbizdate"));
		if(rs.getObject("VORDERENTRYID")!=null){
		    boxrptinfo.setASSEMBLE(rs.getObject("VORDERENTRYID").toString());
		}
		
		boxrptinfo.setCOMBINATION(rs.getObject("customer").toString());
		boxrptinfo.setPPDAREA(rs.getObject("Aamount").toString());
		boxrptinfo.setALAREA(rs.getObject("fprice").toString());
		boxrptinfo.setPRODUCTTYPE(rs.getObject("Alarea").toString());
		boxrptinfo.setAmountrate(rs.getObject("xlarea").toString());
		boxrptinfo.setORDERAMT(rs.getObject("ordertype").toString());
		boxrptinfo.setCHENGPINARAE(rs.getObject("person").toString());
		boxrptinfo.setAMOUNT(rs.getObject("amount").toString());
		boxrptinfo.setCARDBOARDARAE(rs.getObject("entrysum").toString());
		boxrptinfo.setPRODUCTNAME(rs.getObject("area").toString());
		boxrptinfo.setSEQ(rs.getObject("Costs").toString());
		boxrptinfo.setNumber(rs.getObject("fseq").toString());
		boxrptinfo.setORDERTYPE(rs.getObject("salenumber").toString());
		if(rs.getObject("Afid")!=null){
		    boxrptinfo.setWORKPROC(rs.getObject("Afid").toString());
		}
		
		boxrptinfo.setMATERIALCODE(rs.getObject("productid").toString());
		boxrptinfo.setName(rs.getObject("fbrowsegroupid").toString());
//		boxrptinfo.setLOSS(rs.getObject("loss").toString());
		boxrptinfo.setFNUMBER(rs.getObject("TranslateQty").toString());
		boxrptinfo.setSimpleName("0");//
		boxrptinfo.setCreateTime(new Timestamp((new Date()).getTime()));
		pk= BoxYieldRptFactory.getLocalInstance(ctx).addnew(boxrptinfo);
		System.out.println(pk.toString());
		//baCustColl.add(boxrptinfo);


	    }
		
	    } catch (BOSException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
		catch (SQLException e)
		{
		    try {
			throw new BOSException(e);
		    } catch (BOSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    }
		} catch (EASBizException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
//		if(lossid.size()>0){
//		    StringBuffer sb1 = new StringBuffer();
//
//			int inNum1 = 1; //已拼装IN条件数量
//			for(int i=0; i<lossid.size(); i++) {
//
//			    //if(StringUtil.isEmpty(orderlist.get(i))) continue;
//			    
//			    
//			    if(i == (lossid.size()-1))
//			        sb1.append("'" + lossid.get(i) + "'");    //SQL拼装，最后一条不加“,”。
//			    else if(inNum1==20 && i>0) {
//			        sb1.append("'" + lossid.get(i) + "' ) OR SaleorderEntryId  IN ( ");    //解决ORA-01795问题
//			        inNum1 = 1;
//			    }
//			    else {
//			        sb1.append("'" + lossid.get(i) + "', ");
//			        inNum1++;
//			    }
//
//			}
//		    //iDest.delete(" where SaleorderEntryId in "+DJStringHelper.buildIdStringFromList(id)+" ");IN ( " + sb.toString() + " )");
//			try {
//			    iDest = BoxYieldRptFactory.getLocalInstance(ctx);
//			    iDest.delete(" where simplename=0 and SaleorderEntryId IN ( " + sb1.toString() + " )");
//			} catch (EASBizException e) {
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//			} catch (BOSException e) {
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//			}
//		}
//		if (baCustColl.size() > 0) {
//		    // 批量保存
//		    //this.submitBatchData(ctx, baCustColl);
//
//		  //  iDest.submit(baCustColl);
//		}
	
    }


    private void setLossDate(Context ctx) throws BOSException, EASBizException {
	// TODO Auto-generated method stub
	String beginDate;
	String endDate;
	Calendar calendar = Calendar.getInstance();	
	calendar.add(Calendar.DATE, -2 );
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.SECOND,0);
	calendar.set(Calendar.MINUTE,0);
	Calendar calendar1 = Calendar.getInstance();	
	calendar1.add(Calendar.DATE,0 );
	calendar1.set(Calendar.HOUR_OF_DAY, 0);
	calendar1.set(Calendar.SECOND,0);
	calendar1.set(Calendar.MINUTE,0);	
	Date resultDate = calendar.getTime(); 	
	Date resultDate1 = calendar1.getTime(); 
	SimpleDateFormat beginDates = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");  
	endDate=beginDates.format(resultDate1);	
	beginDate=beginDates.format(resultDate);
	IBoxYieldRpt iDest = BoxYieldRptFactory.getLocalInstance(ctx);
	CoreBaseCollection baCustColl = new CoreBaseCollection();
	
	
	
	
	StringBuffer sql = new StringBuffer();
	BigDecimal auamount = new BigDecimal("0");	
	List orderlist = new ArrayList(); 
	List lossid = new ArrayList(); 
	//StringBuffer sql = new StringBuffer();
	 sql.append(" /*dialect*/ select ss.fbrowsegroupid,ss.fbizdate,nvl(ss.VORDERENTRYID,'') VORDERENTRYID,ss.customer,nvl(ss.Aamount,0)Aamount,nvl(ss.fprice,0)fprice,nvl(ss.TranslateQty,0)TranslateQty,nvl(ss.Alarea,0)Alarea,nvl(ss.xlarea,0)xlarea,nvl(ss.ordertype,0)ordertype,nvl(ss.person,'')person,ss.ids,nvl(ss.amount,0)amount,nvl(ss.entrysum,0)entrysum,nvl(ss.area,0)area,nvl(sum(ss.cc),0) Costs,ss.fseq,ss.salenumber,nvl(ss.Afid,'')Afid,ss.productid from   \r\n");
	    sql.append(" (select  t7.fbrowsegroupid,t1.fbizdate,nvl(t.FVORDERENTRYID,ae.fvorderentryid) VORDERENTRYID,t7.fname_l2 customer,t11.FUnitPrice fprice,t.FTranslateQty TranslateQty,t1.fordertype ordertype,t.fseq fseq,ae.fid Afid,t18.fname_l2 person,t7.fname_l2,t1.fnumber salenumber,t.fid ids,t.famount amount,   \r\n");
	    sql.append("t11.FUnitPrice*(t.famount-nvl(t.FTranslateQty,0))  entrysum , \r\n");
	    sql.append("      t14.fmateriallength*t14.fmaterialwidth/10000 xlarea,ta14.fmateriallength*ta14.fmaterialwidth/10000 Alarea,ae.famount Aamount,   \r\n");
	    sql.append("     ----t5.fmateriallength,t5.fmaterialwidth,t5.fbedivisor,t.famount,t5.fdivisor,     \r\n");
	    sql.append("     t.famount*(case when t14.farea is null or t14.feffected = 0 then t2.farea else t14.farea end ) area,  \r\n");
	    sql.append("     p.fnumber,  (case when pcp.FPrice is null  then p.funitcost else pcp.FPrice end )*ml.Frate cc,t2.fid productid                   \r\n");
	    sql.append("    from t_ord_saleorderentry t left join    \r\n");
	    sql.append("    t_ord_saleorder t1 on t.fparentid=t1.fid   \r\n");
	    sql.append("    left join t_pdt_productdef t2 on t.fproductid=t2.fid   \r\n");
	    sql.append("    left join t_bal_gathering t6 on t6.forderentryid=t.fid and t6.fbiztype=41   \r\n");
	    sql.append("     left join t_bd_customer t7 on t7.fid = t1.fcustomerid  left join  T_PDT_ProductProperty t8 on t8.forderentryid = t.fid   \r\n");
	    sql.append("      left join t_bal_balanceprice t9 on t9.fproductid = t2.fid    \r\n");
	    sql.append("     left join (select fparentid,min(FPrice) FPrice from T_BAL_BalancePricePrices group by fparentid) t10 on t9.fid = t10.fparentid    \r\n");
	    sql.append("     left join t_bal_gathering t11 on t11.forderentryid=t.fid  and t11.fbiztype=41   \r\n");
	    sql.append("     left join t_bd_customersaleinfo t16 on t16.fcustomerid = t7.fid   \r\n");
	    sql.append("    left join t_bd_customersaler t17 on t17.fcustomersaleid = t16.fid   \r\n");
	    sql.append("    left join t_bd_person t18 on t18.fid = t17.fpersonid  \r\n");
	    sql.append("     left join t_pdt_productproperty t14 on t14.forderentryid = t.fid  \r\n");
	    sql.append("    -- left join T_PPL_SingleCutPlan s on s.fsaleorderentryid =t.fid  \r\n");
	    sql.append("       left join T_PDT_MaterialCode m on m.fid = t2.FMaterialCodeID                                                \r\n");
	    sql.append("     left join T_PDT_MaterialCodePaperLayers ml on ml.fparentid = m.fid   \r\n");
	    sql.append("    left join T_PDT_PaperCode p on p.fid = ml.fpapercodeid  --   \r\n");
	    sql.append("    left join (select fparentid,min(Cfqprice) FPrice from CT_PDT_PaperCodePrice group by fparentid) pcp on p.fid = pcp.fparentid  --原纸代码单位成本   \r\n");
	    sql.append("     left join t_ord_saleorderentry ae on t.faorderentryid = ae.fid   \r\n");
	    sql.append("     left join t_pdt_productproperty ta14 on ta14.forderentryid = ae.fid  \r\n");
	    sql.append("      --inner join T_PRO_cardboardtask task on task.fsaleorderid=t.fid   \r\n"); // and t1.fnumber ='P1603100661'and t1.fnumber ='P1603100661'and t1.fbizdate >= to_date('2015-12-27','yyyy-mm-dd') AND t1.fbizdate < to_date('2015-12-28','yyyy-mm-dd')
	    sql.append("     where   t1.faudited = 1   and  ( t1.fnumber not like 'V%'and t1.fnumber not like 'A%' and t1.fnumber not like 'S%')    \r\n");
	    
//	    sql.append(" and  T1.Fbiztime > to_date('2015-06-15,00:00:00', 'yyyy-mm-dd hh24:mi:ss') "); //25-4月 -16 09.21.28.800000 上午
//		sql.append(" and T1.Fbiztime <=to_date('2015-07-01,00:00:00', 'yyyy-mm-dd hh24:mi:ss') "); 
	    sql.append(" and  t1.fbizdate > to_date('"+beginDate+"', 'yyyy-mm-dd hh24:mi:ss') ");
	    sql.append(" and t1.fbizdate <=to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss') "); 
	  //  sql.append(" and t7.fid='NbksCwEVEADgAiScwKgCZ78MBA4=' and  t1.fbizdate > to_date('2016-04-10,00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
	   // sql.append(" and t1.fbizdate <=to_date('2016-04-10,00:00:00', 'yyyy-mm-dd hh24:mi:ss')"); 
	    sql.append("      order by t1.fnumber) ss    group by ss.fbrowsegroupid,ss.fbizdate,ss.VORDERENTRYID,ss.Aamount,ss.person,ss.ids,ss.amount,ss.entrysum,ss.area,ss.fseq,ss.salenumber,ss.Afid,ss.ordertype,ss.xlarea,ss.Alarea,ss.TranslateQty,ss.productid,ss.fprice,ss.customer order by ss.salenumber ,ss.fseq desc  \r\n");
	    IRowSet 	rs = SQLExecutorFactory.getLocalInstance(ctx,sql.toString()).executeSQL();
	    try
		{
	    for(;rs.next();)
	    {
		System.out.println(" saleorder count======= "+rs.size());
		if(iDest.exists("where simplename=0 and SaleorderEntryId='"+rs.getObject("ids").toString()+"'"))
		{
		    lossid.add(rs.getObject("ids").toString());
		}

		//select ss.fbizdate,ss.VORDERENTRYID,ss.customer,ss.Aamount,ss.fprice,ss.TranslateQty,ss.Alarea,ss.xlarea,ss.ordertype,ss.person,ss.ids,ss.amount,ss.entrysum,ss.area,sum(ss.cc) Costs,ss.fseq,ss.salenumber,ss.Afid,ss.productid 
		BoxYieldRptInfo boxrptinfo = new BoxYieldRptInfo();
		boxrptinfo.setSaleorderEntryId(rs.getObject("ids").toString());
		boxrptinfo.setPdtIndetailBizTime((Date) rs.getObject("fbizdate"));
		if(rs.getObject("VORDERENTRYID")!=null){
		    boxrptinfo.setASSEMBLE(rs.getObject("VORDERENTRYID").toString());
		}
		
		boxrptinfo.setCOMBINATION(rs.getObject("customer").toString());
		boxrptinfo.setPPDAREA(rs.getObject("Aamount").toString());
		boxrptinfo.setALAREA(rs.getObject("fprice").toString());
		boxrptinfo.setPRODUCTTYPE(rs.getObject("Alarea").toString());
		boxrptinfo.setAmountrate(rs.getObject("xlarea").toString());
		boxrptinfo.setORDERAMT(rs.getObject("ordertype").toString());
		boxrptinfo.setCHENGPINARAE(rs.getObject("person").toString());
		boxrptinfo.setAMOUNT(rs.getObject("amount").toString());
		boxrptinfo.setCARDBOARDARAE(rs.getObject("entrysum").toString());
		boxrptinfo.setPRODUCTNAME(rs.getObject("area").toString());
		boxrptinfo.setSEQ(rs.getObject("Costs").toString());
		boxrptinfo.setNumber(rs.getObject("fseq").toString());
		boxrptinfo.setORDERTYPE(rs.getObject("salenumber").toString());
		if(rs.getObject("Afid")!=null){
		    boxrptinfo.setWORKPROC(rs.getObject("Afid").toString());
		}
		
		boxrptinfo.setMATERIALCODE(rs.getObject("productid").toString());
		boxrptinfo.setName(rs.getObject("fbrowsegroupid").toString());
//		boxrptinfo.setLOSS(rs.getObject("loss").toString());
		boxrptinfo.setFNUMBER(rs.getObject("TranslateQty").toString());
		boxrptinfo.setSimpleName("0");//
		boxrptinfo.setCreateTime(new Timestamp((new Date()).getTime()));
		baCustColl.add(boxrptinfo);


	    }
		}
		catch (SQLException e)
		{
		    throw new BOSException(e);
		}
		if(lossid.size()>0){
		    StringBuffer sb1 = new StringBuffer();

			int inNum1 = 1; //已拼装IN条件数量
			for(int i=0; i<lossid.size(); i++) {

			    //if(StringUtil.isEmpty(orderlist.get(i))) continue;
			    
			    
			    if(i == (lossid.size()-1))
			        sb1.append("'" + lossid.get(i) + "'");    //SQL拼装，最后一条不加“,”。
			    else if(inNum1==20 && i>0) {
			        sb1.append("'" + lossid.get(i) + "' ) OR SaleorderEntryId  IN ( ");    //解决ORA-01795问题
			        inNum1 = 1;
			    }
			    else {
			        sb1.append("'" + lossid.get(i) + "', ");
			        inNum1++;
			    }

			}
		    //iDest.delete(" where SaleorderEntryId in "+DJStringHelper.buildIdStringFromList(id)+" ");IN ( " + sb.toString() + " )");
			iDest.delete(" where simplename=0 and SaleorderEntryId IN ( " + sb1.toString() + " )");
		}
		if (baCustColl.size() > 0) {
		    // 批量保存
		    this.submitBatchData(ctx, baCustColl);

		    //iDest.submit(baCustColl);
		}
	
    }


    private void setBoxYieldDate(Context ctx) throws BOSException, EASBizException {
	// TODO Auto-generated method stub
	String beginDate;
	String endDate;
	Calendar calendar = Calendar.getInstance();	
	calendar.add(Calendar.DATE, -3 );
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.SECOND,0);
	calendar.set(Calendar.MINUTE,0);
	Calendar calendar1 = Calendar.getInstance();	
	calendar1.add(Calendar.DATE,0 );
	calendar1.set(Calendar.HOUR_OF_DAY, 0);
	calendar1.set(Calendar.SECOND,0);
	calendar1.set(Calendar.MINUTE,0);	
	Date resultDate = calendar.getTime(); 	
	Date resultDate1 = calendar1.getTime(); 
	SimpleDateFormat beginDates = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");  
	endDate=beginDates.format(resultDate1);	
	beginDate=beginDates.format(resultDate);
	IBoxYieldRpt iDest = BoxYieldRptFactory.getLocalInstance(ctx);
	CoreBaseCollection baCustColl = new CoreBaseCollection();
	IObjectPK pk = null;
	StringBuffer sql = new StringBuffer();
	BigDecimal auamount = new BigDecimal("0");	
	List orderlist = new ArrayList(); 
	List id = new ArrayList(); 
	sql.append(" /*dialect*/ select  t3.fnumber from  t_ord_saleorderentry  t2      \r\n");                                                                                                                                                                                                                                      
	sql.append("  left join(select forderentryid, max(fbiztime) as fbiztime,sum(FAmount) as FAmount from t_inv_productindetail group by forderentryid ) t1 on t1.forderentryid=t2.fid      \r\n");                                                                                                                                          
	sql.append("       LEFT  JOIN t_ord_saleorder T3 ON t2.fparentid = t3.fid   \r\n");
	sql.append("       left join t_bd_customer t7 on t7.fid = t3.fcustomerid  \r\n");
	sql.append("         where t3.fnumber not like '%A%' and t3.fnumber not like '%B%'   \r\n");
	sql.append(" and  T1.Fbiztime > to_date('"+beginDate+"', 'yyyy-mm-dd hh24:mi:ss') "); //25-4月 -16 09.21.28.800000 上午
	sql.append(" and T1.Fbiztime <=to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss') "); 
	//sql.append(" and  T1.Fbiztime > to_date('2015-06-15,00:00:00', 'yyyy-mm-dd hh24:mi:ss') "); //25-4月 -16 09.21.28.800000 上午
	//sql.append(" and T1.Fbiztime <=to_date('2015-07-01,00:00:00', 'yyyy-mm-dd hh24:mi:ss') "); 
	sql.append("        group by t3.fnumber order by t3.fnumber asc  \r\n");
	IRowSet 	rs1 = SQLExecutorFactory.getLocalInstance(ctx,sql.toString()).executeSQL();
	try
	{
	    System.out.println(" saleorder count======= "+rs1.size());
	    for(;rs1.next();)
	    {
		//System.out.println(rs1.getObject("fnumber"));
		orderlist.add(rs1.getObject("fnumber").toString());
	    }
	}
	catch (SQLException e)
	{
	    throw new BOSException(e);
	}
	StringBuffer sb = new StringBuffer();

	int inNum = 1; //已拼装IN条件数量
	for(int i=0; i<orderlist.size(); i++) {

	    //if(StringUtil.isEmpty(orderlist.get(i))) continue;
	    
	    //这里不要犯低级错误而写成：if(i == custNOs.length)
	    if(i == (orderlist.size()-1))
	        sb.append("'" + orderlist.get(i) + "'");    //SQL拼装，最后一条不加“,”。
	    else if(inNum==20 && i>0) {
	        sb.append("'" + orderlist.get(i) + "' ) OR t3.fnumber  IN ( ");    //解决ORA-01795问题
	        inNum = 1;
	    }
	    else {
	        sb.append("'" + orderlist.get(i) + "', ");
	        inNum++;
	    }

	}
	sql=new StringBuffer(" /*dialect*/	select orderentryid,Fbiztime,FAssemble,FCombination, ranslateqty,nvl(ppdarea,0)ppdarea,nvl(Alarea,0)Alarea,ProductType,nvl(Famountrate,0)Famountrate,nvl(orderamt,0)orderamt,nvl(chengpinArae,0)chengpinArae,nvl(famount,0)famount,nvl(cardboardArae,0)cardboardArae,productname,fseq,fnumber,fordertype,nvl(workProc,0)workProc,nvl(materialCode,'')materialCode,nvl(tileModel,'')tileModel,nvl((cardboardArae-chengpinArae)/decode(cardboardArae,0,1,cardboardArae),0) loss                                                                                                                              \r\n");
	sql.append(" 	 from (select t2.fid orderentryid,T1.Fbiztime,t5.FAssemble,t5.FCombination,nvl(T2.ftranslateqty,0) ranslateqty,T5.FArea ppdarea,t2.FEntryProductType ProductType,T2.Famountrate, nvl(t1.famount,0)famount ,t3.fnumber ordernumber,T5.fname_l2 productname,t2.fseq,t3.fordertype,t3.fnumber,tuik.auamount ,t9.Wmaterialspec,t9.Lmaterialspec,t2.fid saleorderentryid,t9.FAmount AFAmount,nvl(T7.fname_l2,' ')workProc, nvl(T6.fname_l2,' ') materialCode,nvl(t8.fname_l2,' ') tileModel,t2.famount orderamt,t1.famount inamt,    \r\n");
	sql.append(" 	   ((b.fmateriallength- nvl(d.FLengthAdd,0))*(b.fmaterialwidth-nvl(d.fwidthAdd,0))/10000)*(b.fbedivisor/b.fdivisor) Alarea,trunc(nvl(((nvl(property.fmateriallength,b.fmateriallength)- nvl(d.FLengthAdd,0))*(nvl(property.fmaterialwidth,b.fmaterialwidth)-nvl(d.fwidthAdd,0))/10000)*(b.fbedivisor/b.fdivisor) *t1.famount,0),4) chengpinArae,                                                                                              \r\n");
	sql.append(" 	 trunc((nvl((((t9.Wmaterialspec*t9.Lmaterialspec)/10000)*t9.FAmount),0) -nvl((((t9.Wmaterialspec*t9.Lmaterialspec)/10000)*tuik.auamount),0)),4 )cardboardArae,                                                                                                                                                       \r\n");
	sql.append(" 	nvl(property.fmateriallength,b.fmateriallength),nvl(property.fmaterialwidth,b.fmaterialwidth), d.FLengthAdd,b.fmaterialwidth,d.fwidthAdd,b.fbedivisor,b.fdivisor                                                                                                                                                     \r\n");
	sql.append(" 		 FROM t_ord_saleorderentry  T2--((133.9.2-0*47.7.4-0)/10000)*(1/1)*30                                                                                                                                                                                                                                            \r\n");
	sql.append(" 	 left join(select forderentryid, max(fbiztime) as fbiztime,sum(FAmount) as FAmount from t_inv_productindetail group by forderentryid ) t1 on t1.forderentryid=t2.fid                                                                                                                                                 \r\n");
	sql.append(" 	   LEFT OUTER JOIN t_ord_saleorder T3 ON t2.fparentid = t3.fid                                                                                                                                                                                                                                                       \r\n");
	sql.append(" 	  LEFT OUTER JOIN t_pdt_productproperty T4 ON t4.forderentryid = t2.fid                                                                                                                                                                                                                                              \r\n");
	sql.append(" 	 LEFT OUTER JOIN T_PDT_ProductDef T5 ON t5.fid =t2.FProductID                                                                                                                                                                                                                                                        \r\n");
	sql.append(" 	  LEFT OUTER JOIN T_PDT_MaterialCode T6 ON t6.fid = t5.FMaterialCodeID                                                                                                                                                                                                                                               \r\n");
	sql.append(" 	  LEFT OUTER JOIN T_PDT_WorkProcManage T7 ON t7.fid = t5.FWorkProcID                                                                                                                                                                                                                                                 \r\n");
	sql.append(" 	  LEFT OUTER JOIN T_PDT_TileModel T8 ON t8.fid =t5.FTileModelID                                                                                                                                                                                                                                                      \r\n");
	sql.append(" 	  -- LEFT OUTER JOIN T_BD_Customer T18 ON t3.FCustomerID = t18.fid                                                                                                                                                                                                                                                   \r\n");
	sql.append(" 	--left join t_ord_saleorderentry ae on t2.faorderentryid = ae.fid                                                                                                                                                                                                                                                    \r\n");
	sql.append(" 	 left join (select  sum(other.FAmount) FAmount,other.Forderentryid,                                                                                                                                                                                                                                                  \r\n");
	sql.append(" 	 substr(salee.fmaterialspec,1,instr(salee.fmaterialspec,'×')-1) Wmaterialspec,substr(salee.fmaterialspec,                                                                                                                                                                                                            \r\n");
	sql.append(" 	 instr(salee.fmaterialspec,'×')+1,length(salee.fmaterialspec)-instr(salee.fmaterialspec,'×')) Lmaterialspec                                                                                                                                                                                                          \r\n");
	sql.append(" 	  from T_INV_OtherLibrary other  left join t_ord_saleorderentry salee on salee.fid=other.forderentryid  where other.FIssueEnumsID <> 'dKkAAAIDGzC8Pqm+' group by other.Forderentryid,salee.fmaterialspec  ) t9  on t9.forderentryid = t2.faorderentryid                                                              \r\n");
	sql.append(" 	 left join ( select sum(tuik.FAmount) auamount ,tuik.forderentryid      from T_INV_OtherLibrary tuik  where tuik.FIssueEnumsID ='dKkAAAIDGzC8Pqm+' group by tuik.forderentryid ) tuik  on tuik.forderentryid= t2.faorderentryid                                                                                      \r\n");
	sql.append(" 	 left join T_PDT_cardboardworkProc b on t5.fid = b.fproductid                                                                                                                                                                                                                                                        \r\n");
	sql.append(" 	 left join T_PDT_EmborderScheme d on d.fid=t5.FSchemeID                                                                                                                                                                                                                                                              \r\n");
	sql.append(" 	 LEFT OUTER JOIN t_pdt_productproperty  property ON property.forderentryid = t2.fid                                                                                                                                                                                                                                  \r\n");
	//sql.append(" where  t3.fnumber='C1603251350' ");
	sql.append(" where  t3.fnumber IN ( " + sb.toString() + " )");
	//sql.append(" where  t3.fnumber in "+DJStringHelper.buildIdStringFromList(orderlist)+" "); IN ( " + sb.toString() + " )";
	//		sql.append(" where  T1.Fbiztime > to_date('"+beginDates+"', 'yyyy-mm-dd hh24:mi:ss') ");
	//		sql.append(" and T1.Fbiztime <=to_date('"+finishDate+"', 'yyyy-mm-dd hh24:mi:ss') "); 
	//sql.append(" 	 where t2.fid='N6as0ZpGSzSnpg4wm5KOer+aLA0=' --T1.Fbiztime>to_date('2016-01-01','yyyy-mm-dd') and T1.Fbiztime<to_date('2016-02-01','yyyy-mm-dd')                                                                                                                                                                     \r\n");
	sql.append(" 	  and t7.fname_l2 not like '%贸易产品%' and t7.fname_l2 not like '%面板%'   and t7.fname_l2 not like '%费用产品 %'and t7.fname_l2 not like '箱片%'                                                                                                                           \r\n");
	sql.append(" 	  and t7.fname_l2 <> '入库' order by t3.fnumber,t2.fseq desc )       \r\n"); 
	IRowSet 	rs = SQLExecutorFactory.getLocalInstance(ctx,sql.toString()).executeSQL();
	try
	{
	    BigDecimal Arae= new BigDecimal("0");
	    BigDecimal ordermount= new BigDecimal("0");

	    for(;rs.next();)
	    {
		if(iDest.exists("where simplename=1 and SaleorderEntryId='"+rs.getObject("orderentryid").toString()+"'"))
		{
		    BoxYieldRptInfo info = BoxYieldRptFactory.getLocalInstance(ctx).getBoxYieldRptInfo("where simplename=1 and SaleorderEntryId='"+rs.getObject("orderentryid").toString()+"'");
		    id.add(info.getId().toString());
		}

		//FAssemble,FCombination, ranslateqty,ppdarea,Alarea,ProductType,Famountrate,orderamt,chengpinArae,famount,cardboardArae,productname,fseq,fnumber,fordertype,workProc,materialCode,tileModel,(
		BoxYieldRptInfo boxrptinfo = new BoxYieldRptInfo();
		boxrptinfo.setSaleorderEntryId(rs.getObject("orderentryid").toString());
		boxrptinfo.setPdtIndetailBizTime((Date) rs.getObject("Fbiztime"));
		boxrptinfo.setASSEMBLE(rs.getObject("FAssemble").toString());
		boxrptinfo.setCOMBINATION(rs.getObject("FCombination").toString());
		boxrptinfo.setPPDAREA(rs.getObject("ppdarea").toString());
		boxrptinfo.setALAREA(rs.getObject("Alarea").toString());
		boxrptinfo.setPRODUCTTYPE(rs.getObject("ProductType").toString());
		boxrptinfo.setAmountrate(rs.getObject("Famountrate").toString());
		boxrptinfo.setORDERAMT(rs.getObject("orderamt").toString());
		boxrptinfo.setCHENGPINARAE(rs.getObject("chengpinArae").toString());
		boxrptinfo.setAMOUNT(rs.getObject("famount").toString());
		boxrptinfo.setCARDBOARDARAE(rs.getObject("cardboardArae").toString());
		boxrptinfo.setPRODUCTNAME(rs.getObject("productname").toString());
		boxrptinfo.setSEQ(rs.getObject("fseq").toString());
		boxrptinfo.setNumber(rs.getObject("fnumber").toString());
		boxrptinfo.setORDERTYPE(rs.getObject("fordertype").toString());
		boxrptinfo.setWORKPROC(rs.getObject("workProc").toString());
		boxrptinfo.setMATERIALCODE(rs.getObject("materialCode").toString());
		boxrptinfo.setName(rs.getObject("tileModel").toString());
		boxrptinfo.setLOSS(rs.getObject("loss").toString());
		boxrptinfo.setFNUMBER(rs.getObject("ranslateqty").toString());
		boxrptinfo.setSimpleName("1");//
		boxrptinfo.setCreateTime(new Timestamp((new Date()).getTime()));
		//pk= BoxYieldRptFactory.getLocalInstance(ctx).addnew(boxrptinfo);
		baCustColl.add(boxrptinfo);
		


	    }
	}
	catch (SQLException e)
	{
	    throw new BOSException(e);
	}
	if(id.size()>0){
	    StringBuffer sb1 = new StringBuffer();

		int inNum1 = 1; //已拼装IN条件数量
		for(int i=0; i<id.size(); i++) {

		    //if(StringUtil.isEmpty(orderlist.get(i))) continue;
		    
		    //这里不要犯低级错误而写成：if(i == custNOs.length)
		    if(i == (id.size()-1))
		        sb1.append("'" + id.get(i) + "'");    //SQL拼装，最后一条不加“,”。
		    else if(inNum1==20 && i>0) {
		        sb1.append("'" + id.get(i) + "' ) OR id  IN ( ");    //解决ORA-01795问题
		        inNum1 = 1;
		    }
		    else {
		        sb1.append("'" + id.get(i) + "', ");
		        inNum1++;
		    }

		}
	    //iDest.delete(" where SaleorderEntryId in "+DJStringHelper.buildIdStringFromList(id)+" ");IN ( " + sb.toString() + " )");
		iDest.delete(" where   id IN ( " + sb1.toString() + " )");
	}
	if (baCustColl.size() > 0) {
	    // 批量保存
	    this.submitBatchData(ctx, baCustColl);

	    //iDest.submit(baCustColl);
	}
	
    }
    /**
     * yg 2016年6月17日 15:45:11 自动核对
     */
   
    protected void _doBoardOrderCheck(Context ctx) throws BOSException {
	// TODO Auto-generated method stub
	String updatesql="/*dialect*/ update (select en.fchecked from t_ord_saleorderentry en inner join t_ord_saleorder s on en.fparentid=s.fid where s.Fbrdboxtype = 1  and s.faudited=1 and (s.fnumber like 'A%' or s.fnumber like 'V%') and fchecked=0 ) set fchecked=1 ";
	DbUtil.execute(ctx,updatesql);
	DbUtil.execute(ctx,"/*dialect*/ update(select sen.fchecked From t_ord_saleorder sa inner join t_ord_saleorderentry sen on sen.fparentid=sa.fid where sa.Fbrdboxtype = 1  and sa.faudited=1 and sa.fordertype=1  and sa.fnumber not like 'A%' and sa.fnumber not like 'V%' and sen.fchecked = 0 and not exists (select en.fid from t_ord_saleorder s inner join t_ord_saleorderentry en on en.fparentid=s.fid where s.Fbrdboxtype = 1 and s.fordertype=1 and s.fnumber not like 'A%' and s.fnumber not like 'V%' and sa.fcustomerid=s.fcustomerid and sen.fproductspec=en.fproductspec and sen.fid<>en.fid and s.fcreatetime > trunc(sysdate) ))set fchecked = 1");	
	updatesql="/*dialect*/ update t_ord_saleorderentry set fchecked=1 where fparentid in (select se.fid from t_ord_saleorderentry sen inner join t_ord_saleorder se on sen.fparentid=se.fid ";
	updatesql=updatesql+" where sen.fchecked=0 and se.fordertype=2  and se.faudited=1 and sen.fseq=1 and se.fbrdboxtype=1 and se.fnumber not like 'A%' and se.fnumber not like 'V%' ";
	updatesql=updatesql+" and not exists (select en.fid from t_ord_saleorderentry en inner join t_ord_saleorder s on en.fparentid=s.fid  where  s.fordertype=2 and en.fseq=1  and s.fbrdboxtype=1 and s.fnumber not like 'A%' and s.fnumber not like 'V%' ";
	updatesql=updatesql+" and se.fcustomerid=s.fcustomerid and sen.fproductspec= en.fproductspec and sen.fid<>en.fid and s.fcreatetime > trunc(sysdate)))";
	DbUtil.execute(ctx,updatesql);
	
		
	 try {
	    initlogin(ctx);
	} catch (EASBizException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
		
	
    }
   
    public void doBoardOrderCheck(Context ctx) throws BOSException {
	// TODO Auto-generated method stub
	super.doBoardOrderCheck(ctx);
    }
    public void initlogin(Context ctx) throws EASBizException, BOSException, SQLException{
	    
	  
	//直接更新纸板A单和V单的核单标志
//	
	/**
	 * 1- 记录日志
	 * **/
	StringBuffer oql = new StringBuffer("select r.fname_l2 CustomerName,  " );
	oql.append("en.fmaterialspec MaterialSpec, ");
	oql.append("en.FManufQty ManufQty, ");
	oql.append("s.fnumber SaleOrderID, ");
	oql.append("s.fauditdate AuditDate, ");
	oql.append("ert.fname_l2 AuditorName, ");
	oql.append("er.fname_l2 SingleName ");
	oql.append("from t_ord_saleorderentry en ");
	oql.append(" inner join t_ord_saleorder s on en.fparentid = s.fid  ");
	oql.append("  left join t_bd_customer r on r.fid = s.fcustomerid  ");
	oql.append("  LEFT join t_pm_user ert on ert.fid = s.fauditorid  ");
	oql.append("  LEFT join t_pm_user er on er.fid = s.fcreatorid  ");
	oql.append("  where s.Fbrdboxtype = 1   ");
	oql.append(" and s.faudited = 1   ");
	oql.append(" and (s.fnumber like 'A%' or s.fnumber like 'V%')  ");
	oql.append(" and fchecked = 0  ");
	IRowSet rs = null; 
	try
	{
		//rs = SQLExecutorFactory.getRemoteInstance(oql.toString()).executeSQL();
	     rs = ReportReadWritePart.executeQuery(ctx, oql.toString());
	}
	catch (BOSException el)
	{	
		el.printStackTrace();
	}
	BoardOrderCheckRecordInfo BoardOrderinfo =new BoardOrderCheckRecordInfo();
	if(rs != null)
	{
		while (rs.next())
		{
		    BoardOrderinfo.setSaleOrderID(rs.getObject("SaleOrderID").toString());
		    BoardOrderinfo.setAuditorName((String)rs.getObject("AuditorName"));
		    BoardOrderinfo.setSingleName((String)rs.getObject("SingleName"));
		    //
		    BoardOrderinfo.setCheckName(SysContext.getSysContext().getCurrentUserInfo().getName());
			
		    BoardOrderinfo.setCustomerName(rs.getObject("CustomerName").toString());
		    BoardOrderinfo.setMaterialSpec(rs.getObject("MaterialSpec").toString());
			
		    BoardOrderinfo.setManufQty((BigDecimal) rs.getObject("ManufQty"));
		    BoardOrderinfo.setAuditDate((Date) rs.getObject("AuditDate"));
		    //
			
		    Calendar calendar = Calendar.getInstance();
		    BoardOrderinfo.setCheckedtime(calendar.getTime());
			
		    BoardOrderCheckRecordFactory.getLocalInstance(ctx).addnew(BoardOrderinfo);

		}
	}

	/**
	 * 2- 记录日志
	 * **/
	StringBuffer oql_2 = new StringBuffer("select r.fname_l2 CustomerName, " );
	oql_2.append("en.fmaterialspec MaterialSpec, ");
	oql_2.append("en.FManufQty ManufQty , ");
	oql_2.append("s.fnumber SaleOrderID, ");
	oql_2.append("s.fauditdate AuditDate, ");
	oql_2.append("ert.fname_l2 AuditorName, ");
	oql_2.append("er.fname_l2 SingleName ");
	oql_2.append("from t_ord_saleorderentry en ");
	oql_2.append("inner join t_ord_saleorder s on en.fparentid = s.fid ");
	oql_2.append("left join t_bd_customer r on r.fid = s.fcustomerid ");
	oql_2.append("LEFT join t_pm_user ert on ert.fid = s.fauditorid ");
	oql_2.append("LEFT join t_pm_user er on er.fid = s.fcreatorid ");
	oql_2.append("where s.Fbrdboxtype = 1 ");
	oql_2.append("and s.faudited = 1 ");
	oql_2.append("and s.fnumber not like 'A%' ");
	oql_2.append("and s.fnumber not like 'V%' ");
	oql_2.append("and s.fordertype = 1 ");
	oql_2.append("and en.fchecked = 0 ");
	oql_2.append("and not exists ");
	oql_2.append("(select en.fid ");
	oql_2.append(" from t_ord_saleorder ssd ");
	oql_2.append(" inner join t_ord_saleorderentry en on en.fparentid = ssd.fid ");
	oql_2.append(" where ssd.Fbrdboxtype = 1 ");
	oql_2.append(" and ssd.fordertype = 1 ");
	oql_2.append(" and ssd.fnumber not like 'A%' ");
	oql_2.append(" and ssd.fnumber not like 'V%' ");
	oql_2.append(" and s.fcustomerid = s.fcustomerid ");
	oql_2.append(" and en.fproductspec = en.fproductspec ");
	oql_2.append(" and en.fid <> en.fid ");
	oql_2.append(" and ssd.fcreatetime > trunc(sysdate) ) ");
	IRowSet rs_2 = null; 
	try
	{
	    rs_2 = ReportReadWritePart.executeQuery(ctx, oql_2.toString());
	}
	catch (BOSException el)
	{	
		el.printStackTrace();
	}
	if(rs_2 != null)
	{
	    while (rs_2.next())
		{
		    BoardOrderinfo.setSaleOrderID(rs_2.getObject("SaleOrderID").toString());
		    BoardOrderinfo.setAuditorName((String)rs_2.getObject("AuditorName"));
		    BoardOrderinfo.setSingleName((String)rs_2.getObject("SingleName"));
		    //
		    BoardOrderinfo.setCheckName(SysContext.getSysContext().getCurrentUserInfo().getName());
			
		    BoardOrderinfo.setCustomerName(rs_2.getObject("CustomerName").toString());
		    BoardOrderinfo.setMaterialSpec(rs_2.getObject("MaterialSpec").toString());
			
		    BoardOrderinfo.setManufQty((BigDecimal) rs_2.getObject("ManufQty"));
		    BoardOrderinfo.setAuditDate((Date) rs_2.getObject("AuditDate"));
		    //
			
		    Calendar calendar = Calendar.getInstance();
		    BoardOrderinfo.setCheckedtime(calendar.getTime());
			
		    BoardOrderCheckRecordFactory.getLocalInstance(ctx).addnew(BoardOrderinfo);

		}   
	}
	
    
    /**
     * 3- 记录日志
     * */
   StringBuffer oql_3 = new StringBuffer("select r.fname_l2 CustomerName, " );
   oql_3.append("en.fmaterialspec MaterialSpec,  ");
   oql_3.append("en.FManufQty ManufQty , ");
   oql_3.append("s.fnumber SaleOrderID, ");
   oql_3.append("s.fauditdate AuditDate, ");
   oql_3.append("ert.fname_l2 AuditorName, ");
   oql_3.append("er.fname_l2 SingleName ");
   oql_3.append("from t_ord_saleorderentry en ");
   oql_3.append("inner join t_ord_saleorder s on en.fparentid = s.fid ");
   oql_3.append("left join t_bd_customer r on r.fid = s.fcustomerid ");
   oql_3.append("LEFT join t_pm_user ert on ert.fid = s.fauditorid ");
   oql_3.append("LEFT join t_pm_user er on er.fid = s.fcreatorid ");
   oql_3.append("where en.fparentid in ");
   oql_3.append("(select se.fid ");
   oql_3.append("from t_ord_saleorderentry sen ");
   oql_3.append("inner join t_ord_saleorder se on sen.fparentid = se.fid ");
   oql_3.append("where sen.fchecked = 0 ");
   oql_3.append("and se.fordertype = 2 ");
   oql_3.append("and se.faudited = 1 ");
   oql_3.append("and sen.fseq = 1 ");
   oql_3.append("and se.fbrdboxtype = 1 ");
   oql_3.append("and se.fnumber not like 'A%' ");
   oql_3.append("and se.fnumber not like 'V%' ");
   oql_3.append("and not exists ");
   oql_3.append("(select en.fid ");
   oql_3.append("from t_ord_saleorderentry en ");
   oql_3.append("inner join t_ord_saleorder s on en.fparentid = s.fid ");
   oql_3.append("where s.fordertype = 2 ");
   oql_3.append("and en.fseq = 1 ");
   oql_3.append("and s.fbrdboxtype = 1 ");
   oql_3.append("and s.fnumber not like 'A%' ");
   oql_3.append("and s.fnumber not like 'V%' ");
   oql_3.append("and se.fcustomerid = s.fcustomerid ");
   oql_3.append("and sen.fproductspec = en.fproductspec ");
   oql_3.append("and sen.fid <> en.fid ");
   oql_3.append("and s.fcreatetime > trunc(sysdate) )) ");
   IRowSet rs_3 = null; 
	try
	{
	    rs_3 = ReportReadWritePart.executeQuery(ctx, oql_3.toString());
	}
	catch (BOSException el)
	{	
		el.printStackTrace();
	}
	if(rs_3!=null)
	{
	    while (rs_3.next())
		{
		    BoardOrderinfo.setSaleOrderID(rs_3.getObject("SaleOrderID").toString());
		    BoardOrderinfo.setAuditorName((String)rs_3.getObject("AuditorName"));
		    BoardOrderinfo.setSingleName((String)rs_3.getObject("SingleName"));
		    //
		    BoardOrderinfo.setCheckName(SysContext.getSysContext().getCurrentUserInfo().getName());
		    
		    BoardOrderinfo.setCustomerName(rs_3.getObject("CustomerName").toString());
		    BoardOrderinfo.setMaterialSpec(rs_3.getObject("MaterialSpec").toString());
			
		    BoardOrderinfo.setManufQty((BigDecimal) rs_3.getObject("ManufQty"));
		    BoardOrderinfo.setAuditDate((Date) rs_3.getObject("AuditDate"));
		    //
			
		    Calendar calendar = Calendar.getInstance();
		    BoardOrderinfo.setCheckedtime(calendar.getTime());
			
		    BoardOrderCheckRecordFactory.getLocalInstance(ctx).addnew(BoardOrderinfo);

		} 

	}
}

    
    
}