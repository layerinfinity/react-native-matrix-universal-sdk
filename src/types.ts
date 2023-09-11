export type HomeServerConfigType = {
  homeServerUrl: string;
  deviceId?: string;
  accessToken?: string;
};

export type LoginParamsType = {
  homeServerUrl: string;
  username: string;
  password: string;
};

export type LoginJwtParamsType = {
  homeServerUrl: string;
  jwtToken: string;
};

export type RoomType = {
  roomId: string;
  displayName: string;
  lastMessage: string;
};
