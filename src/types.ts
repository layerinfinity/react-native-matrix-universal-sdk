export type HomeServerConfigType = {
  homeServerUrl: string;
  deviceId: string;
  accessToken: string;
};

export type LoginParamsType = {
  username: string;
  password: string;
};

export type RoomType = {
  roomId: string;
  displayName: string;
  lastMessage: string;
};
