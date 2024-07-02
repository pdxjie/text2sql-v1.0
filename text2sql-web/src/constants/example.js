export const exampleRules = [
  {
    id: 1,
    content: `-- 客户表
CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY IDENTITY(1,1), -- 客户 ID，自增主键
    FullName NVARCHAR(100) NOT NULL,          -- 客户全名
    PhoneNumber NVARCHAR(15) NOT NULL,        -- 电话号码
    Email NVARCHAR(100),                      -- 电子邮件
    Address NVARCHAR(200)                     -- 地址
) COMMENT '客户表';

-- 房间表
CREATE TABLE Rooms (
    RoomID INT PRIMARY KEY IDENTITY(1,1),     -- 房间 ID，自增主键
    RoomNumber NVARCHAR(10) NOT NULL,         -- 房间号码
    RoomType NVARCHAR(50) NOT NULL,           -- 房间类型（如单人间，双人间，套房等）
    PricePerNight DECIMAL(10, 2) NOT NULL,    -- 每晚价格
    Availability BIT NOT NULL DEFAULT 1       -- 可用性（1: 可用, 0: 不可用）
) COMMENT '房间表';

-- 预订表
CREATE TABLE Reservations (
    ReservationID INT PRIMARY KEY IDENTITY(1,1),  -- 预订 ID，自增主键
    CustomerID INT NOT NULL,                      -- 客户 ID
    RoomID INT NOT NULL,                          -- 房间 ID
    CheckInDate DATE NOT NULL,                    -- 入住日期
    CheckOutDate DATE NOT NULL,                   -- 退房日期
    TotalAmount DECIMAL(10, 2) NOT NULL,          -- 总金额
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),  -- 外键关联客户表
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) -- 外键关联房间表
) COMMENT '预订表';`
  },
  {
    id: 2,
    content: `-- 会员表
CREATE TABLE Members (
    MemberID INT PRIMARY KEY IDENTITY(1,1), -- 会员 ID，自增主键
    FullName NVARCHAR(100) NOT NULL,        -- 会员全名
    PhoneNumber NVARCHAR(15) NOT NULL,      -- 电话号码
    Email NVARCHAR(100),                    -- 电子邮件
    JoinDate DATE NOT NULL                  -- 加入日期
) COMMENT '会员表';

-- 台球桌表
CREATE TABLE Tables (
    TableID INT PRIMARY KEY IDENTITY(1,1),  -- 台球桌 ID，自增主键
    TableNumber NVARCHAR(10) NOT NULL,      -- 台球桌编号
    TableType NVARCHAR(50) NOT NULL,        -- 台球桌类型（如斯诺克，美式落袋等）
    PricePerHour DECIMAL(10, 2) NOT NULL,   -- 每小时价格
    Availability BIT NOT NULL DEFAULT 1     -- 可用性（1: 可用, 0: 不可用）
) COMMENT '台球桌表';

-- 预订表
CREATE TABLE Reservations (
    ReservationID INT PRIMARY KEY IDENTITY(1,1),  -- 预订 ID，自增主键
    MemberID INT NOT NULL,                        -- 会员 ID
    TableID INT NOT NULL,                         -- 台球桌 ID
    ReservationDate DATE NOT NULL,                -- 预订日期
    StartTime TIME NOT NULL,                      -- 开始时间
    EndTime TIME NOT NULL,                        -- 结束时间
    TotalAmount DECIMAL(10, 2) NOT NULL,          -- 总金额
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID),  -- 外键关联会员表
    FOREIGN KEY (TableID) REFERENCES Tables(TableID)      -- 外键关联台球桌表
) COMMENT '预订表';`
  },
  {
    id: 3,
    content: `-- 客户表
CREATE TABLE Customers (
  CustomerID INT PRIMARY KEY IDENTITY(1,1), -- 客户 ID，自增主键
  FullName NVARCHAR(100) NOT NULL,          -- 客户全名
  PhoneNumber NVARCHAR(15) NOT NULL,        -- 电话号码
  Email NVARCHAR(100),                      -- 电子邮件
  Address NVARCHAR(200)                     -- 地址
) COMMENT '客户表';

-- 旅游路线表
CREATE TABLE Tours (
  TourID INT PRIMARY KEY IDENTITY(1,1),     -- 旅游路线 ID，自增主键
  TourName NVARCHAR(100) NOT NULL,          -- 旅游路线名称
  Destination NVARCHAR(100) NOT NULL,       -- 目的地
  DurationDays INT NOT NULL,                -- 行程天数
  Price DECIMAL(10, 2) NOT NULL             -- 价格
) COMMENT '旅游路线表';

-- 预订表
CREATE TABLE Reservations (
  ReservationID INT PRIMARY KEY IDENTITY(1,1),  -- 预订 ID，自增主键
  CustomerID INT NOT NULL,                      -- 客户 ID
  TourID INT NOT NULL,                          -- 旅游路线 ID
  ReservationDate DATE NOT NULL,                -- 预订日期
  NumberOfPeople INT NOT NULL,                  -- 预订人数
  TotalAmount DECIMAL(10, 2) NOT NULL,          -- 总金额
  FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),  -- 外键关联客户表
  FOREIGN KEY (TourID) REFERENCES Tours(TourID)              -- 外键关联旅游路线表
) COMMENT '预订表';`
  },
  {
    id: 4,
    content: `-- 用户表
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),  -- 用户 ID，自增主键
    FullName NVARCHAR(100) NOT NULL,       -- 用户全名
    PhoneNumber NVARCHAR(15) NOT NULL,     -- 电话号码
    Email NVARCHAR(100),                   -- 电子邮件
    Address NVARCHAR(200)                  -- 地址
) COMMENT '用户表';

-- 充电桩表
CREATE TABLE ChargingStations (
    StationID INT PRIMARY KEY IDENTITY(1,1),  -- 充电桩 ID，自增主键
    Location NVARCHAR(200) NOT NULL,          -- 充电桩位置
    Capacity INT NOT NULL,                    -- 充电桩容量（千瓦）
    Availability BIT NOT NULL DEFAULT 1       -- 可用性（1: 可用, 0: 不可用）
) COMMENT '充电桩表';

-- 充电记录表
CREATE TABLE ChargingRecords (
    RecordID INT PRIMARY KEY IDENTITY(1,1),   -- 充电记录 ID，自增主键
    UserID INT NOT NULL,                      -- 用户 ID
    StationID INT NOT NULL,                   -- 充电桩 ID
    StartTime DATETIME NOT NULL,              -- 开始时间
    EndTime DATETIME NOT NULL,                -- 结束时间
    EnergyConsumed DECIMAL(10, 2) NOT NULL,   -- 消耗电量（千瓦时）
    TotalCost DECIMAL(10, 2) NOT NULL,        -- 总费用
    FOREIGN KEY (UserID) REFERENCES Users(UserID),        -- 外键关联用户表
    FOREIGN KEY (StationID) REFERENCES ChargingStations(StationID)  -- 外键关联充电桩表
) COMMENT '充电记录表';`
  }
]
