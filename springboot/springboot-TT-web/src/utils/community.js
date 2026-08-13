export const DEFAULT_COMMUNITY_ID = "2022081539020475";

export function getCommunityId() {
  return localStorage.getItem("tt_community_id") || DEFAULT_COMMUNITY_ID;
}

export function setCommunityId(id) {
  if (id) {
    localStorage.setItem("tt_community_id", id);
  }
}
