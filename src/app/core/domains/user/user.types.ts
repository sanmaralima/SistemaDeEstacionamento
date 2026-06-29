export interface RegisterRequest {
  username: string;
  password?: string;
}

export interface UserUpdateRequest {
  name: string;
  email: string;
}

export interface UserRoleRequest {
  role: string;
}

export interface UserResponse {
  id: string;
  name: string;
  email?: string;
  role: string;
  companyId: string;
}

export interface UpdateUserParams {
  id: string;
  request: UserUpdateRequest;
}

export interface UpdateUserRoleParams {
  id: string;
  request: UserRoleRequest;
}

export interface CreateCollaboratorParams {
  companyId: string;
  request: RegisterRequest;
}
