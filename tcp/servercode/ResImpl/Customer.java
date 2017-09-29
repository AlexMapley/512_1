// -------------------------------




  private int m_nID;
  private RMHashtable m_Reservations;

  Customer(int id) {
    super();
    m_Reservations = new RMHashtable();
    
  }

  public void setID(int id)
    m_nID=id;
  }

  public int getID()
    return m_nID;
  }

  public void reserve(String key, String location, int price)
    ReservedItem reservedItem = getReservedItem(key);
    if(reservedItem == null) {
      // customer doesn't already have a reservation for this resource, so create a new one now
      reservedItem = new ReservedItem(key, location, 1, price);
    
    else {
      reservedItem.setCount(reservedItem.getCount() + 1);
      reservedItem.setPrice(price);
    }
    m_Reservations.put(reservedItem.getKey(), reservedItem);
  }

  public ReservedItem getReservedItem(String key)
    ReservedItem reservedItem = (ReservedItem) m_Reservations.get(key);
    return reservedItem;
  }

  public String printBill()
    String s = "Bill for customer " + m_nID + "\n";
    Object key = null;
    for (Enumeration e = m_Reservations.keys(); e.hasMoreElements(); ) {
      key = e.nextElement();
      ReservedItem item = (ReservedItem) m_Reservations.get( key );
      s = s + item.getCount() + " " + item.getReservableItemKey() + " $" + item.getPrice() + "\n";
    }
    return s;
  }

  public String toString()
    return "--- BEGIN CUSTOMER key='" + getKey() + "', id='" + getID()
    + "', reservations=>\n" +
    +
  }

  public static String getKey(int customerID)
    String s = "customer-" + customerID;
    return s.toLowerCase();
  }

  public String getKey()
    return Customer.getKey( getID() );
  }

  public RMHashtable getReservations()
    try {
      return (RMHashtable)  m_Reservations;
    }
    catch( Exception e ) {
      return null;
    } // catch
  }
}