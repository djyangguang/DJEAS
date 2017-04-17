public class StatisticalQueryRpt extends AbstractStatisticalQueryRpt
{
    private static final Logger logger = CoreUIObject.getLogger(StatisticalQueryRpt.class);
    private static HashMap regionMap = new HashMap();
    /**
     * output class constructor
     */
    public StatisticalQueryRpt() throws Exception
    {
        super();
    }
    boolean isonload =false;
	


    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }

    
	public void onLoad() throws Exception
	{
		
		super.onLoad();
		tblMain.checkParsed();
		btnSearch.setEnabled(true);
		actionSearch.setEnabled(true);
		this.btnSearch.setIcon(EASResource.getIcon("imgTbtn_filter"));
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		toolTipManager.setDismissDelay(30000);
		this.btnSearch.setToolTipText("<html><p><font size=4 face=Verdana><font color=#CE0000>报表逻辑：筛选超过6个月未被领用，新模板资料中状态不是报废与销毁且仓库不等于外协模切板仓库的平模。</font></p></html>" );
		kDTree1.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				if (e.getNewLeadSelectionPath() != null) {
					try {
						if(!isonload){
							AccessoryTree_valueChanged(e);
						}else{
							isonload=false;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}


		
		});
		//AccessoryTypeEnum
		//buildeAccessoryTree();
		buildeAccessoryTree();
	}
	

	private void buildeAccessoryTree() {
	    // TODO Auto-generated method stub
	    DefaultKingdeeTreeNode root = new DefaultKingdeeTreeNode("版模类型");
	    DefaultKingdeeTreeNode parentrNode = root;
	    DefaultKingdeeTreeNode currentNode = null;
	    
//	    for(AccessoryTypeEnum s : AccessoryTypeEnum.getEnumList()){
//		
//	    }
	    List accessorylist =AccessoryTypeEnum.getEnumList();
	    Class clz = AccessoryTypeEnum.class;
	    for (int i=0;i<accessorylist.size();i++) {
		System.out.println(accessorylist.get(i));
		currentNode = new DefaultKingdeeTreeNode(accessorylist.get(i));
		currentNode.setUserObject(accessorylist.get(i));

		parentrNode.add(currentNode);
	    }
	    kDTree1.setModel(new KingdeeTreeModel(root));
	    kDTree1.setSelectionNode(root);
	    
	}


	
	private void AccessoryTree_valueChanged(
		TreeSelectionEvent e) {
	    // TODO Auto-generated method stub
	    
	}
	private CountDownLatch latch = new CountDownLatch(100);
	private Long total =0L;
	private Integer totalCount=0;
	private class ExecuteThread implements Runnable{
		private Long amount;
		public ExecuteThread(long amount){
			this.amount=amount;
		}
		
		
		public void run() {
			// TODO Auto-generated method stub
			try {
				latch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
			    String reString = "";
			    ISaleorderInfoTable ibst;
			    try {	
				HashMap params = new HashMap();
				HashMap hash = new HashMap();
				List<HashMap> idslist = new ArrayList<HashMap>();
				hash.put("ID", "PzPjS9IhAQrgU38AAAEBCnFz7GM=");
				idslist.add(hash);
				params.put("idslist", idslist);
				//2015-07-17  审核放到事务中进行，所以只修改产品即可
				//hash.put("editproduct", true);
				ibst = SaleorderInfoTableFactory.getRemoteInstance();
				reString = ibst.boardAudit(params);
				System.out.println("子线程"+Thread.currentThread().getName()+"正在执行=============>"+reString);
				if(reString==""){

				    totalCount+=1; //多少人抢到这线程
				}
			    } catch (BOSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			   
			    
			  
			} catch (EASBizException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			
			}
			
		}
		
	}
	public void actionSearch_actionPerformed(ActionEvent e)
		throws Exception {
	    // TODO Auto-generated method stub
//	    super.actionSearch_actionPerformed(e);
	   // AccountListFactory.getRemoteInstance().checkAccountList("s+MDEAEZEADgACUMwKgCZ78MBA4=");
        querys();
           /* for (int i=0;i<100;i++){
		 Thread t = new Thread(new ExecuteThread(3L));
		 t.start();
		 latch.countDown();
	 }
	Thread.currentThread().sleep(3000);*/
//	 System.out.println(totalCount+"人抢到这条记录");
	    /** 
	     * yg 2016年10月19日 15:42:20  读取excel中的信息执行批量插入 数据 
	     * 
	       InputStream is = new FileInputStream("D:/开票数据整理（161018） .xls");
	       String[][] result = ExcelOperate.getData(is, 1);
	      

	       int rowLength = result.length;
	       String Customerid ="";
	       BigDecimal amount=BigDecimal.ZERO;
	       for(int i=0;i<rowLength;i++) {

		   for(int j=0;j<result[i].length;j++) {

		       System.out.print(result[i][j]+"\t\t");
		       Customerid=result[i][0];
		       amount=new BigDecimal(result[i][2]);
		   }
		   InvoiceInfo objectValue = new InvoiceInfo();
		   objectValue.setCreator(UserFactory.getRemoteInstance().getUserInfo(new ObjectUuidPK(BOSUuid.read("00000000-0000-0000-0000-00000000000013B7DE7F"))));
		   objectValue.setBizDate(new Date());
		   objectValue.setType(InvoiceTypeEnum.box);	
		   objectValue.setInvoiceKind(InvoiceKindEnum.special);
		   objectValue.setOutputUnit(OutputUnitEnum.dlzb);
		   objectValue.setInvoiceUnit(InvoiceUnitFactory.getRemoteInstance().getInvoiceUnitInfo(new ObjectUuidPK(BOSUuid.read("dKkAAAIaG6WpHse9"))));
		   CustomerInfo customerInfo = CustomerFactory.getRemoteInstance().getCustomerInfo(new ObjectUuidPK(BOSUuid.read(Customerid)));
		   PersonInfo personInfo=null;
		   try {
		   ISaleOrder iSaleOrder = SaleOrderFactory.getRemoteInstance();
		   personInfo= iSaleOrder.getSaler(customerInfo.getId().toString());
		   } catch (Exception e1) {
			e1.printStackTrace();
		}
		   objectValue.setInvoiceMan(personInfo);
		   objectValue.setCustomer(customerInfo);
		   //ivoiceNum
		   objectValue.setIvoiceNum("0");
		   //invoiceAmt
		   objectValue.setInvoiceAmt(amount);
		   objectValue.setProduct(ProductNameEnum.box);
		   //productUnit
		   objectValue.setProductUnit(ProductUnitEnum.merely);
		   //arBillNumber
		   objectValue.setArBillNumber("2014年及以前数据调整");
		   IObjectPK pk = InvoiceFactory.getRemoteInstance().submit(objectValue);

		   System.out.println();

	       }
	       **/
	}

	private void querys() {
	    // TODO Auto-generated method stub
	    try {
		BoxYieldRptFactory.getRemoteInstance().SteDate();
	    } catch (EASBizException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (BOSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}


	//	protected void query() throws BOSException, SQLException
//	{
//
//        try
//		{
//	  String sql =" /*dialect*/ select cus.fname_l2  customer,t.fnumber modelCode ,wh.fname_l2  warehouse,ws.fname warehousesites ,t.flastusedate  lastTime "+
//			" ,pdef.fname_l2  product from t_acs_printplatenew  t  left join t_bd_customer cus on t.fcustomerid=cus.fid  "+
//			" inner join t_inv_warehouse wh on wh.fid=t.fwarehouseid   inner join t_inv_warehousesites ws on ws.fid=t.fwarehousesitid  "+
//	    	"	left join t_pdt_productdefdrawing pdw on pdw.fid=t.fpaperdrawid 	left join t_pdt_productrelatedrawing prd on prd.fdrawingid=pdw.fid "+
//	    	"	left join t_pdt_productdef pdef on prd.fproductid=pdef.fid  	where t.fstates not in (3,4) and t.flastusedate is not null  "+
//	    	"	and t.flastusedate <=add_months(sysdate,-6) and t.fpatterntype=2 ";
//			    IRowSet rs = null; 
//				rs = SQLExecutorFactory.getRemoteInstance(sql.toString()).executeSQL();
//				tblMain.removeRows();
//				while(rs.next())
//				{
//			
//		    	IRow row = tblMain.addRow();
//				row.getCell("customer").setValue(rs.getObject("customer").toString());
//				row.getCell("modelCode").setValue(rs.getObject("modelCode").toString());
//				row.getCell("warehouse").setValue(rs.getObject("warehouse").toString());
//				row.getCell("warehousesites").setValue(rs.getObject("warehousesites").toString());
//				row.getCell("lastTime").setValue(rs.getObject("lastTime").toString());
//				row.getCell("product").setValue(rs.getObject("product").toString());
//		    	} 
//		}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//	
//		}
	public void query() {
		// 获取数据
		try {
			RptParams params = getRemoteInstance().query(getParamsForInit());
			IRowSet rs = (IRowSet) params.getObject("Statistical_Range");							
			tblMain.removeRows();
			while(rs.next())
			{
				IRow row = tblMain.addRow();
				if(rs.getObject("customer")==null){
					row.getCell("customer").setValue("");
				}
				else{
					row.getCell("customer").setValue(rs.getObject("customer"));//客户名称
				}
				
				row.getCell("modelCode").setValue(rs.getObject("modelCode").toString());//版模编码
				row.getCell("warehouse").setValue(rs.getObject("warehouse").toString());//仓库名称
				row.getCell("warehousesites").setValue(rs.getObject("warehousesites").toString());//库位编码
				row.getCell("lastTime").setValue(rs.getObject("lastTime").toString());//最近领用时间
				if(rs.getObject("product")==null){
					row.getCell("product").setValue("");
				}
				else
				{
				row.getCell("product").setValue(rs.getObject("product"));//产品
				}
				if(rs.getObject("person")==null)
				{
					row.getCell("person").setValue("");
				}
				else{
					row.getCell("person").setValue(rs.getObject("person").toString());//业务员
			
				}
				
			}
	} 
		catch (Exception e) 
	{
		handleException(e);
	}
	}
	/**
	 * yg 2016年4月10日 08:15:38 新增 版模类型
	 * @return
	 */
	  protected RptParams getParamsForInit() 
	    {
	      
			RptParams params = new RptParams();
			params.setObject("Statistical", "Statistical_Range");
			DefaultTreeSelectionModel model = (DefaultTreeSelectionModel) kDTree1.getSelectionModel();
			if (model == null) {
				SysUtil.abort();
			}
			int accessoryType =0;
			DefaultKingdeeTreeNode node = (DefaultKingdeeTreeNode) model.getSelectionPath().getLastPathComponent();
			if (node.isLeaf()) {
			    String name = node.getText();
				if(("圆模").equals(name))
					accessoryType=1;
				if(("平模").equals(name))
					accessoryType=2;
				if(("柔版").equals(name))
					accessoryType=3;
				if(("PS版").equals(name))
					accessoryType=4;
				if(("样本").equals(name))
					accessoryType=5;
				if(("胶片").equals(name))
					accessoryType=6;
				if(("油墨").equals(name))
					accessoryType=7;
				if(("图纸").equals(name))
					accessoryType=8;
				if(("皮板").equals(name))
					accessoryType=9;
				params.setObject("accessory", accessoryType);
			}
			return params;
		}

		protected ICommRptBase getRemoteInstance() throws BOSException 
		{
			return CustomerBalanceRptFacadeFactory.getRemoteInstance();
		}
	


	/**
     * output actionPageSetup_actionPerformed
     */
    public void actionPageSetup_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionPageSetup_actionPerformed(e);
    }

    /**
     * output actionExitCurrent_actionPerformed
     */
    public void actionExitCurrent_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionExitCurrent_actionPerformed(e);
    }

    /**
     * output actionHelp_actionPerformed
     */
    public void actionHelp_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionHelp_actionPerformed(e);
    }

    /**
     * output actionAbout_actionPerformed
     */
    public void actionAbout_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionAbout_actionPerformed(e);
    }

    /**
     * output actionOnLoad_actionPerformed
     */
    public void actionOnLoad_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionOnLoad_actionPerformed(e);
    }

    /**
     * output actionSendMessage_actionPerformed
     */
    public void actionSendMessage_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionSendMessage_actionPerformed(e);
    }

    /**
     * output actionCalculator_actionPerformed
     */
    public void actionCalculator_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionCalculator_actionPerformed(e);
    }

    /**
     * output actionExport_actionPerformed
     */
    public void actionExport_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionExport_actionPerformed(e);
    }

    /**
     * output actionExportSelected_actionPerformed
     */
    public void actionExportSelected_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionExportSelected_actionPerformed(e);
    }

    /**
     * output actionRegProduct_actionPerformed
     */
    public void actionRegProduct_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionRegProduct_actionPerformed(e);
    }

    /**
     * output actionPersonalSite_actionPerformed
     */
    public void actionPersonalSite_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionPersonalSite_actionPerformed(e);
    }

    /**
     * output actionProcductVal_actionPerformed
     */
    public void actionProcductVal_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionProcductVal_actionPerformed(e);
    }

    /**
     * output actionExportSave_actionPerformed
     */
    public void actionExportSave_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionExportSave_actionPerformed(e);
    }

    /**
     * output actionExportSelectedSave_actionPerformed
     */
    public void actionExportSelectedSave_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionExportSelectedSave_actionPerformed(e);
    }

}